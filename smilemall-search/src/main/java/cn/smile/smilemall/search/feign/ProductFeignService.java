package cn.smile.smilemall.search.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-20
 */
@SmileFeign
@FeignClient(name = "cn-smile-smilemall-product" )
public interface ProductFeignService {
	
	/**
	 * <p>远程调用查询属性信息</p>
	 * @author smile
	 * @date 2021/2/20/020
	 * @param attrId 1
	 * @return cn.smile.common.utils.R
	 */
	@RequestMapping("/product/attr/info/{attrId}")
	public R info(@PathVariable("attrId") Long attrId) ;
	
	/**
	 * <p>获取所有品牌信息</p>
	 * @author smile
	 * @date 2021/2/21/021
	 * @param brandIds 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/product/brand/brandInfo")
	public R brandInfos(@RequestBody List<Long> brandIds) ;
}
