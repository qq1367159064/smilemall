package cn.smile.smilemall.product.feignImpl;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.to.es.SkuEsModule;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>检索服务远程调用</p>
 * @author smile
 * @date 2021-02-15
 */
@SmileFeign
@FeignClient(value = "cn-smile-smilemall-gateway", contextId = "esFeign")
public interface EsFeignService {
	
	/**
	 * <p>商品上架，保存检索信息到es</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param skuEsModules 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/api/search/save/es/productUp")
	public R productStatusUp(@RequestBody List<SkuEsModule> skuEsModules) ;
}
