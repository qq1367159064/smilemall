package cn.smile.smilemall.cart.smilemallcart.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>远程调用商品服务</p>
 *
 * @author smile
 * @date 2021-02-26
 */
@SmileFeign
@FeignClient(name = "cn-smile-smilemall-product")
public interface ProductFeignService {
	
	/**
	 * <p>获取sku信息</p>
	 * @author smile
	 * @date 2021/2/26/026
	 * @param skuId 1
	 * @return cn.smile.common.utils.R
	 */
	@RequestMapping("product/skuinfo//info/{skuId}")
	public R info(@PathVariable("skuId") Long skuId);
	
	/**
	 * <p>获取sku属性信息</p>
	 * @author smile
	 * @date 2021/2/26/026
	 * @param skuId 1
	 * @return java.util.List<java.lang.String>
	 */
	@GetMapping("/product/skusaleattrvalue/getSkuSaleAttrValue/{skuId}")
	public List<String> getSkuSaleAttrValue(@PathVariable("skuId") Long skuId);
	
	/**
	 * <p>获取商品价格</p>
	 * @author smile
	 * @date 2021/2/28/028
	 * @param skuId 1
	 * @return java.math.BigDecimal
	 */
	@GetMapping("/product/skuinfo/getSkuPrice/{skuId}")
	public String getSkuPrice(@PathVariable(value = "skuId") Long skuId);
}
