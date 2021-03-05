package cn.smile.smilemall.ware.service.impl;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.common.utils.R;
import cn.smile.smilemall.ware.dao.WareSkuDao;
import cn.smile.smilemall.ware.entity.WareSkuEntity;
import cn.smile.smilemall.ware.exception.NotStockException;
import cn.smile.smilemall.ware.feign.ProdcutFeign;
import cn.smile.smilemall.ware.service.WareSkuService;
import cn.smile.smilemall.ware.vo.OrderItemVo;
import cn.smile.smilemall.ware.vo.SkuHasStockVo;
import cn.smile.smilemall.ware.vo.WareSkuLockVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
	private final ProdcutFeign prodcutFeign;
	
	public WareSkuServiceImpl(ProdcutFeign prodcutFeign) {
		this.prodcutFeign = prodcutFeign;
	}
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		
		QueryWrapper<WareSkuEntity> wareSkuEntityQueryWrapper = new QueryWrapper<>();
		Integer skuId = !StringUtils.isEmpty(params.get("skuId")) ? Integer.parseInt(params.get("skuId").toString()) : null;
		if (!StringUtils.isEmpty(skuId)) {
			wareSkuEntityQueryWrapper.eq("sku_id", skuId);
		}
		Integer wareId = !StringUtils.isEmpty(params.get("wareId")) ? Integer.parseInt(params.get("wareId").toString()) :
				null;
		if (!StringUtils.isEmpty(skuId)) {
			wareSkuEntityQueryWrapper.eq("ware_id", wareId);
		}
		
		IPage<WareSkuEntity> page = this.page(
				new Query<WareSkuEntity>().getPage(params),
				wareSkuEntityQueryWrapper
		);
		
		return new PageUtils(page);
	}
	
	/**
	 * @param skuId  1
	 * @param wareId 2
	 * @param skuNum 3
	 * @return boolean
	 * @Description 商品入库
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean addStock(Long skuId, Long wareId, Integer skuNum) {
		WareSkuEntity wareSkuEntity = new WareSkuEntity();
		wareSkuEntity.setSkuId(skuId);
		wareSkuEntity.setWareId(wareId);
		wareSkuEntity.setStock(skuNum);
		wareSkuEntity.setStockLocked(0);
		R info = prodcutFeign.info(skuId);
		if (info.getCode() == 0) {
			Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
			wareSkuEntity.setSkuName(skuInfo.get("skuName").toString());
		}
		if (!this.baseMapper.addStock(skuId, wareId, skuNum)) {
			this.save(wareSkuEntity);
		}
		return true;
		
	}
	
	
	/**
	 * <p>判断是否有库存</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param ids 1
	 * @return  List<SkuHasStockVo>
	 */
	@Override
	public List<SkuHasStockVo> getSkuHasStock(List<Long> ids) {
		return ids.stream().map(skuId -> {
			SkuHasStockVo hasStockVo = new SkuHasStockVo();
			Long skuHasStock = this.baseMapper.getSkuHasStock(skuId);
			if (skuHasStock == null || skuHasStock <= 0) {
				hasStockVo.setHasStock(false);
			} else {
				hasStockVo.setHasStock(true);
			}
			hasStockVo.setSkuId(skuId);
			return hasStockVo;
		}).collect(Collectors.toList());
	}
	
	@Transactional
	@Override
	public Boolean orderLockStock(WareSkuLockVo wareSkuLockVo) {
		
		// TODO 根据收货地址锁定就近仓库的库存
		
		List<OrderItemVo> orderItemVos = wareSkuLockVo.getOrderItemVos();
		List<SkuWareHasStock> wareHasStocks = orderItemVos.stream().map(item -> {
			SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
			Long skuId = item.getSkuId();
			skuWareHasStock.setSkuId(skuId);
			skuWareHasStock.setNum(item.getCount());
			List<Long> longs = baseMapper.listWareIdHasSkuStock(skuId);
			skuWareHasStock.setWareId(longs);
			return skuWareHasStock;
		}).collect(Collectors.toList());
		
		for (SkuWareHasStock wareHasStock : wareHasStocks) {
			Boolean skuStocked = false;
			Long skuId = wareHasStock.getSkuId();
			List<Long> wareIds = wareHasStock.getWareId();
			if(wareIds == null || wareIds.size() == 0) {
				throw new NotStockException(skuId);
			}
			for (Long wareId : wareIds) {
				if (!baseMapper.lockSkuStock(skuId, wareId, wareHasStock.getNum())) {
					continue;
				} else {
					skuStocked = true;
					break;
				}
			}
			if(!skuStocked) {
				throw new NotStockException(skuId);
			}
		}
		return true;
	}
	
	@Data
	class SkuWareHasStock {
		private Long skuId;
		private Integer num;
		private List<Long> wareId;
	}
}