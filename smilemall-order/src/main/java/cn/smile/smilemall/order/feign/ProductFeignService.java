package cn.smile.smilemall.order.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-03
 */
@SmileFeign
@FeignClient(value = "cn-smile-smilemall-product")
public interface ProductFeignService {
	
	/**
	 * <p>获取spu信息</p>
	 * @author smile
	 * @date 2021/3/3/003
	 * @param skuId 1
	 * @return cn.smile.common.utils.R
	 */
	@GetMapping("/product/spuinfo/getSpuInfoBySkuId")
	public R getSpuInfoBySkuId(@PathVariable(value = "skuId") Long skuId) ;
}
