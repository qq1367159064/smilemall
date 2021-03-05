package cn.smile.smilemall.cart.smilemallcart.services.imp;

import cn.smile.common.smilemall.utils.SmileMallRedisTemplate;
import cn.smile.common.utils.R;
import cn.smile.smilemall.cart.smilemallcart.feign.ProductFeignService;
import cn.smile.smilemall.cart.smilemallcart.services.CartService;
import cn.smile.smilemall.cart.smilemallcart.to.UserInfoTo;
import cn.smile.smilemall.cart.smilemallcart.vo.CartItemVo;
import cn.smile.smilemall.cart.smilemallcart.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-25
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {
	
	private final ProductFeignService productFeignService;
	private final ExecutorService threadPoolExecutor;
	
	private final String CART_PREFIX = "smilemall:cart";
	
	public CartServiceImpl(ProductFeignService productFeignService, ExecutorService threadPoolExecutor) {
		this.productFeignService = productFeignService;
		this.threadPoolExecutor = threadPoolExecutor;
	}
	
	
	@Override
	public CartItemVo addCart(Long skuId, Integer number, UserInfoTo userInfoTo) {
		String cartKey = "";
		CartItemVo cartItemVo = new CartItemVo();
		
		if (userInfoTo.getUserId() != null) {
			cartKey = CART_PREFIX + userInfoTo.getUserId();
		} else {
			cartKey = CART_PREFIX + userInfoTo.getUserKey();
		}
		
		BoundHashOperations cartHash = SmileMallRedisTemplate.boundHashOperations(cartKey);
		
		CartItemVo exitCartItemVo = (CartItemVo) cartHash.get(skuId.toString());
		if (exitCartItemVo != null) {
			exitCartItemVo.setCount(exitCartItemVo.getCount() + number);
			cartHash.put(skuId.toString(), exitCartItemVo);
			return exitCartItemVo;
		}
		
		CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
			R skuInfo = productFeignService.info(skuId);
			Map info = (Map) skuInfo.get("skuInfo");
			cartItemVo.setCheck(true);
			cartItemVo.setCount(number);
			cartItemVo.setPrice(BigDecimal.valueOf(Double.valueOf(info.get("price").toString())));
			cartItemVo.setSkuId(skuId);
			cartItemVo.setTitle(info.get("skuTitle").toString());
			cartItemVo.setImage(info.get("skuDefaultImg").toString());
		}, threadPoolExecutor);
		
		CompletableFuture<Void> getSkuSaleAttrValueTask = CompletableFuture.runAsync(() -> {
			List<String> skuSaleAttrValue = productFeignService.getSkuSaleAttrValue(skuId);
			cartItemVo.setSkuAttr(skuSaleAttrValue);
		}, threadPoolExecutor);
		
		try {
			CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValueTask).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		cartHash.put(String.valueOf(skuId), cartItemVo);
		
		return cartItemVo;
	}
	
	@Override
	public CartItemVo getCartItem(Long skuId, UserInfoTo userInfoTo) {
		String cartKey = "";
		if (userInfoTo.getUserId() != null) {
			cartKey = CART_PREFIX + userInfoTo.getUserId();
		} else {
			cartKey = CART_PREFIX + userInfoTo.getUserKey();
		}
		BoundHashOperations cartHash = SmileMallRedisTemplate.boundHashOperations(cartKey);
		return (CartItemVo) cartHash.get(skuId.toString());
	}
	
	@Override
	public CartVo getCartInfo(UserInfoTo userInfoTo) {
		String cartKey = "";
		CartVo cartVo = new CartVo();
		if (userInfoTo.getUserId() != null) {
			cartKey = CART_PREFIX + userInfoTo.getUserId();
			List<CartItemVo> tempCartInfo = getTempCartInfo(CART_PREFIX + userInfoTo.getUserKey());
			if(tempCartInfo != null && tempCartInfo.size() != 0) {
				for (CartItemVo cartItemVo : tempCartInfo) {
					addCart(cartItemVo.getSkuId(), cartItemVo.getCount(), userInfoTo);
				}
			}
			clearCart(CART_PREFIX + userInfoTo.getUserKey());
			List<CartItemVo> cartInfo = getTempCartInfo(cartKey);
			cartVo.setCartItemVos(cartInfo);
			return cartVo;
		} else {
			cartKey = CART_PREFIX + userInfoTo.getUserKey();
			cartVo.setCartItemVos(getTempCartInfo(cartKey));
			return cartVo;
		}
	}
	
	@Override
	public boolean clearCart(String key) {
		SmileMallRedisTemplate.delete(key);
		return true;
	}
	
	@Override
	public void checkItem(Long skuId, int check, UserInfoTo userInfoTo) {
		String cartKey = getCartKey(userInfoTo);
		BoundHashOperations boundHashOperations = SmileMallRedisTemplate.boundHashOperations(cartKey);
		CartItemVo cartItem = (CartItemVo) boundHashOperations.get(skuId.toString());
		cartItem.setCheck(check == 1);
		boundHashOperations.put(skuId.toString(), cartItem);
	}
	
	@Override
	public void countItem(Long skuId, int num, UserInfoTo userInfoTo) {
		String cartKey = getCartKey(userInfoTo);
		BoundHashOperations boundHashOperations = SmileMallRedisTemplate.boundHashOperations(cartKey);
		CartItemVo cartItem = (CartItemVo) boundHashOperations.get(skuId.toString());
		cartItem.setCount(num);
		boundHashOperations.put(skuId.toString(), cartItem);
	}
	
	@Override
	public void deleteItem(Long skId, UserInfoTo userInfoTo) {
		String cartKey = getCartKey(userInfoTo);
		BoundHashOperations boundHashOperations = SmileMallRedisTemplate.boundHashOperations(cartKey);
		boundHashOperations.delete(skId.toString());
	}
	
	@Override
	public List<CartItemVo> getCartHasCheck(UserInfoTo userInfoTo) {
		if (StringUtils.isEmpty(userInfoTo.getUserId())) {
			return new ArrayList<>();
		}
		String cartKey = getCartKey(userInfoTo);
		List<CartItemVo> tempCartInfo = getTempCartInfo(cartKey);
		return tempCartInfo.stream().filter(item -> item.getCheck()).map(itex -> {
			String skuPrice = productFeignService.getSkuPrice(itex.getSkuId());
			itex.setPrice(new BigDecimal(skuPrice));
			return itex;
		}).collect(Collectors.toList());
	}
	
	private List<CartItemVo> getTempCartInfo(String cartKey) {
		BoundHashOperations cartHash = SmileMallRedisTemplate.boundHashOperations(cartKey);
		List<CartItemVo> values = cartHash.values();
		return values;
	}
	
	private String getCartKey(UserInfoTo userInfoTo) {
		String cartKey = "";
		if (userInfoTo.getUserId() != null) {
			cartKey = CART_PREFIX + userInfoTo.getUserId();
		} else {
			cartKey = CART_PREFIX + userInfoTo.getUserKey();
		}
		return cartKey;
	}
}
