package cn.smile.smilemall.order.service.impl;

import cn.smile.common.smilemall.utils.SmileMallRedisTemplate;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.common.utils.R;
import cn.smile.smilemall.order.constant.OrderConstant;
import cn.smile.smilemall.order.dao.OrderDao;
import cn.smile.smilemall.order.entity.OrderEntity;
import cn.smile.smilemall.order.entity.OrderItemEntity;
import cn.smile.smilemall.order.enume.OrderStatusEnum;
import cn.smile.smilemall.order.feign.CartFeignService;
import cn.smile.smilemall.order.feign.MemberFeignService;
import cn.smile.smilemall.order.feign.ProductFeignService;
import cn.smile.smilemall.order.feign.WmsFeignService;
import cn.smile.smilemall.order.interceptor.UserLoginInterceptor;
import cn.smile.smilemall.order.service.OrderItemService;
import cn.smile.smilemall.order.service.OrderService;
import cn.smile.smilemall.order.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author smile
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
	
	private ThreadLocal<OrderSubmitVo> threadLocal = new ThreadLocal<>();
	
	private final MemberFeignService memberFeignService;
	private final CartFeignService cartFeignService;
	private final ThreadPoolExecutor threadPoolExecutor;
	private final WmsFeignService wmsFeignService;
	private final ProductFeignService productFeignService;
	private final OrderItemService orderItemService;
	
	public OrderServiceImpl(MemberFeignService memberFeignService, CartFeignService cartFeignService, ThreadPoolExecutor threadPoolExecutor, WmsFeignService wmsFeignService, ProductFeignService productFeignService, OrderItemService orderItemService) {
		this.memberFeignService = memberFeignService;
		this.cartFeignService = cartFeignService;
		this.threadPoolExecutor = threadPoolExecutor;
		this.wmsFeignService = wmsFeignService;
		this.productFeignService = productFeignService;
		this.orderItemService = orderItemService;
	}
	
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<OrderEntity> page = this.page(
				new Query<OrderEntity>().getPage(params),
				new QueryWrapper<OrderEntity>()
		);
		
		return new PageUtils(page);
	}
	
	
	@Override
	public OrderConfirmVo confirmOrder() {
		OrderConfirmVo confirmVo = new OrderConfirmVo();
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Long userId = UserLoginInterceptor.USER_INFO_THREAD_LOCAL.get().getId();
		CompletableFuture<Void> getAddressTask = CompletableFuture.runAsync(() -> {
			RequestContextHolder.setRequestAttributes(requestAttributes);
			List<MemberAddressVo> addressVos =
					memberFeignService.getAddressByUserId(userId);
			confirmVo.setMemberAddressVos(addressVos);
		}, threadPoolExecutor);
		
		CompletableFuture<Void> getOrderItemVo = CompletableFuture.runAsync(() -> {
			RequestContextHolder.setRequestAttributes(requestAttributes);
			List<OrderItemVo> orderItemVos = cartFeignService.getCartHasCheck();
			confirmVo.setOrderItemVos(orderItemVos);
		}, threadPoolExecutor).thenRunAsync(() -> {
			RequestContextHolder.setRequestAttributes(requestAttributes);
			List<OrderItemVo> itemVos = confirmVo.getOrderItemVos();
			List<Long> orderSkuId = itemVos.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
			R skuHasStock = wmsFeignService.getSkuHasStock(orderSkuId);
			List<Map<String, Object>> list = (List) skuHasStock.get("skuHasStock");
			if (list != null) {
				Map<Long, Object> stock =
						list.stream().collect(Collectors.toMap(key -> Long.parseLong(key.get("skuId").toString()),
								value -> value.get("hasStock")));
				confirmVo.setStocks(stock);
			}
		}, threadPoolExecutor);
		
		confirmVo.setIntegration(UserLoginInterceptor.USER_INFO_THREAD_LOCAL.get().getIntegration());
		String orderToken = UUID.randomUUID().toString().replaceAll("-", "");
		
		SmileMallRedisTemplate.setValue(OrderConstant.USER_ORDER_TOKEN_PREFIX + userId, orderToken, 30L * 60L * 1000L);
		
		confirmVo.setOrderToken(orderToken);
		try {
			CompletableFuture.allOf(getAddressTask, getOrderItemVo).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return confirmVo;
	}
	
	/**
	 * <p>订单构建</p>
	 *
	 * @param orderSubmitVo 1
	 * @return cn.smile.smilemall.order.vo.SubmitOrderResponseVo
	 * @author smile
	 * @date 2021/3/2/002
	 */
	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
		
		threadLocal.set(orderSubmitVo);
		SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();
		submitOrderResponseVo.setCode(0);
		String orderToken = orderSubmitVo.getOrderToken();
		
		if (!SmileMallRedisTemplate.execute(null,
				OrderConstant.USER_ORDER_TOKEN_PREFIX + UserLoginInterceptor.USER_INFO_THREAD_LOCAL.get().getId(),
				orderToken)) {
			submitOrderResponseVo.setCode(1);
			return submitOrderResponseVo;
		}
		
		OrderCreateTo orderCreateTo = orderCreateTo();
		
		BigDecimal payAmount = orderCreateTo.getOrder().getPayAmount();
		BigDecimal payPrice = orderSubmitVo.getPayPrice();
		
		double diff = 0.01;
		if (Math.abs(payAmount.subtract(payPrice).doubleValue()) > diff) {
			submitOrderResponseVo.setCode(2);
			return submitOrderResponseVo;
		}
		
		// 数据持久化
		saveOrder(orderCreateTo);
		
		return lockStock(submitOrderResponseVo, orderCreateTo);
	}
	
	/**
	 * <p>库存锁定</p>
	 * @author smile
	 * @date 2021/3/3/003
	 * @param submitOrderResponseVo 1
	 * @param orderCreateTo 2
	 * @return cn.smile.smilemall.order.vo.SubmitOrderResponseVo
	 */
	private SubmitOrderResponseVo lockStock(SubmitOrderResponseVo submitOrderResponseVo, OrderCreateTo orderCreateTo) {
	
		List<OrderItemVo> lockSkuInfo = orderCreateTo.getOrderItemEntities().stream().map(item -> {
			OrderItemVo orderItemVo = new OrderItemVo();
			orderItemVo.setSkuId(item.getSkuId());
			orderItemVo.setCount(item.getSkuQuantity());
			orderItemVo.setTitle(item.getSkuName());
			return orderItemVo;
		}).collect(Collectors.toList());
		
		WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
		wareSkuLockVo.setOrderItemVos(lockSkuInfo);
		
		R lockR = wmsFeignService.orderLockStock(wareSkuLockVo);
		
		// 如果失败
		if (lockR.getCode() != 0) {
			submitOrderResponseVo.setCode(3);
			return submitOrderResponseVo;
		}
		submitOrderResponseVo.setOrder(orderCreateTo.getOrder());
		return submitOrderResponseVo;
	}
	
	/**
	 * 保存订单数据
	 * @param orderCreateTo
	 */
	private void saveOrder(OrderCreateTo orderCreateTo) {
		
		OrderEntity order = orderCreateTo.getOrder();
		List<OrderItemEntity> orderItemEntities = orderCreateTo.getOrderItemEntities();
		
		this.save(order);
		orderItemService.saveBatch(orderItemEntities);
		
	}
	
	/**
	 * <p>构建订单</p>
	 *
	 * @return cn.smile.smilemall.order.vo.OrderCreateTo
	 * @author smile
	 * @date 2021/3/3/003
	 */
	private OrderCreateTo orderCreateTo() {
		
		OrderSubmitVo orderSubmitVo = threadLocal.get();
		OrderCreateTo createTo = new OrderCreateTo();
		OrderEntity orderEntity = new OrderEntity();
		
		// 生成订单号
		String orderSn = IdWorker.getTimeId();
		// 构建订单
		buildOrder(orderSubmitVo, orderEntity, orderSn);
		// 构建订单项
		List<OrderItemEntity> orderItemEntities = buildOrderItem(orderSn);
		// 计算价格
		computePrice(orderEntity, orderItemEntities);
		// 保存数据
		createTo.setOrderItemEntities(orderItemEntities);
		createTo.setOrder(orderEntity);
		
		return createTo;
	}
	
	/**
	 * <p>计算价格</p>
	 *
	 * @param orderEntity       1
	 * @param orderItemEntities 2
	 * @return void
	 * @author smile
	 * @date 2021/3/3/003
	 */
	private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
		
		BigDecimal totalPrice = new BigDecimal("0.0");
		BigDecimal coupon = new BigDecimal("0.0");
		BigDecimal integration = new BigDecimal("0.0");
		BigDecimal promotion = new BigDecimal("0.0");
		Integer giftIntegration = 0;
		Integer giftGrowth = 0;
		for (OrderItemEntity orderItemEntity : orderItemEntities) {
			totalPrice =
					totalPrice.add(orderItemEntity.getRealAmount());
			coupon = coupon.add(orderItemEntity.getCouponAmount());
			integration = integration.add(orderItemEntity.getIntegrationAmount());
			promotion = promotion.add(orderItemEntity.getPromotionAmount());
			giftIntegration += orderItemEntity.getGiftIntegration();
			giftGrowth += orderItemEntity.getGiftGrowth();
		}
		orderEntity.setTotalAmount(totalPrice);
		orderEntity.setPayAmount(orderEntity.getFreightAmount().multiply(orderEntity.getTotalAmount()));
		orderEntity.setPromotionAmount(promotion);
		orderEntity.setCouponAmount(coupon);
		orderEntity.setIntegrationAmount(integration);
		
		orderEntity.setGrowth(giftGrowth);
		orderEntity.setIntegration(giftIntegration);
		
	}
	
	/**
	 * <p>构建订单项</p>
	 *
	 * @return void
	 * @author smile
	 * @date 2021/3/2/002
	 */
	private List<OrderItemEntity> buildOrderItem(String orderSn) {
		List<OrderItemVo> orderItemVos = cartFeignService.getCartHasCheck();
		if (orderItemVos != null && orderItemVos.size() != 0) {
			List<OrderItemEntity> orderItemEntityList = orderItemVos.stream().map(item -> {
				OrderItemEntity orderItemEntity = new OrderItemEntity();
				buildOrderItem(item, orderItemEntity);
				orderItemEntity.setOrderSn(orderSn);
				return orderItemEntity;
			}).collect(Collectors.toList());
			return orderItemEntityList;
		}
		return null;
	}
	
	/**
	 * <p>构建订单项的纤细信息</p>
	 *
	 * @param item 1
	 * @return cn.smile.smilemall.order.entity.OrderItemEntity
	 * @author smile
	 * @date 2021/3/2/002
	 */
	private void buildOrderItem(OrderItemVo item, OrderItemEntity orderItemEntity) {
		
		orderItemEntity.setSkuId(item.getSkuId());
		orderItemEntity.setSkuName(item.getTitle());
		orderItemEntity.setSkuPic(item.getImage());
		orderItemEntity.setSkuPrice(item.getPrice());
		orderItemEntity.setSkuQuantity(item.getCount());
		orderItemEntity.setSkuAttrsVals(StringUtils.collectionToDelimitedString(item.getSkuAttr(), ";"));
		orderItemEntity.setGiftGrowth((int) (item.getPrice().intValue() * 0.8));
		orderItemEntity.setGiftIntegration((int) (item.getPrice().intValue() * 0.8));
		orderItemEntity.setPromotionAmount(new BigDecimal("0.01"));
		orderItemEntity.setCouponAmount(new BigDecimal("0.0"));
		orderItemEntity.setIntegrationAmount(new BigDecimal("0.0"));
		orderItemEntity.setRealAmount(orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity())));
		
		R spuInfoBySkuId = productFeignService.getSpuInfoBySkuId(item.getSkuId());
		
		Map spuInfo = (Map) spuInfoBySkuId.get("spuInfo");
		
		orderItemEntity.setSpuId(Long.parseLong(spuInfo.get("id").toString()));
		orderItemEntity.setSpuBrand(spuInfo.get("brandId").toString());
		orderItemEntity.setSpuName(spuInfo.get("spuName").toString());
		orderItemEntity.setCategoryId(Long.parseLong(spuInfo.get("catalogId").toString()));
		
	}
	
	/**
	 * <p>构建订单</p>
	 *
	 * @param orderSubmitVo 1
	 * @param orderEntity   2
	 * @param orderSn       3
	 * @return void
	 * @author smile
	 * @date 2021/3/2/002
	 */
	private void buildOrder(OrderSubmitVo orderSubmitVo, OrderEntity orderEntity, String orderSn) {
		orderEntity.setOrderSn(orderSn);
		orderEntity.setCreateTime(new Date());
		
		R r = wmsFeignService.getFare(orderSubmitVo.getAddrId());
		Map faeVo = (Map) r.get("fare");
		
		orderEntity.setFreightAmount(new BigDecimal(faeVo.get("price").toString()));
		Map address = (Map) faeVo.get("address");
		orderEntity.setReceiverCity(address.get("city").toString());
		orderEntity.setReceiverDetailAddress(address.get("detailAddress").toString());
		orderEntity.setReceiverName(address.get("name").toString());
		orderEntity.setReceiverPhone(address.get("phone").toString());
		orderEntity.setReceiverPostCode(address.get("postCode").toString());
		orderEntity.setReceiverRegion(address.get("region").toString());
		orderEntity.setReceiverProvince(address.get("province").toString());
		
		orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
		orderEntity.setCreateTime(new Date());
		orderEntity.setAutoConfirmDay(7);
		orderEntity.setDeleteStatus(0);
		orderEntity.setModifyTime(new Date());
		orderEntity.setMemberId(UserLoginInterceptor.USER_INFO_THREAD_LOCAL.get().getId());
		
	}
}
