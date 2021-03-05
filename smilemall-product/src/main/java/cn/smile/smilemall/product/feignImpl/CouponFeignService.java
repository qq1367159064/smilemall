package cn.smile.smilemall.product.feignImpl;

import cn.smile.common.to.SkuReductionTo;
import cn.smile.common.to.SpuBoundTo;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/24/024
 */
@Service
@FeignClient(value = "cn-smile-smilemall-coupon")
public interface CouponFeignService {
	@RequestMapping("/coupon/spubounds/save")
	public R save(@RequestBody SpuBoundTo spuBoundTo);
	
	@PostMapping("/coupon/skufullreduction/saveInfo")
	R saveSkuReduction(SkuReductionTo skuReductionTo);
}
