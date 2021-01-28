package cn.smile.smilemall.ware.feign;

import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/28/028
 */
@FeignClient(value = "cn-smile-smilemall-product", fallback = ProdcutFeignImpl.class )
@Service
public interface ProdcutFeign {
	@RequestMapping("product/skuinfo/info/{skuId}")
	public R info(@PathVariable("skuId") Long skuId);
}
