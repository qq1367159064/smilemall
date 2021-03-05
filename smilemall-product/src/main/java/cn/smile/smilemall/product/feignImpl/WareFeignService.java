package cn.smile.smilemall.product.feignImpl;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Smile
 * @description
 * @date 2021-02-15
 */
@FeignClient(value = "cn-smile-smilemall-gateway", contextId = "wareFeign")
@SmileFeign
public interface WareFeignService {
	
	/**
	 * <p>库存服务接口,判断是否还有库存</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param ids 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/api/ware/waresku//skuHasStock")
	public R getSkuHasStock(@RequestBody List<Long> ids);
}
