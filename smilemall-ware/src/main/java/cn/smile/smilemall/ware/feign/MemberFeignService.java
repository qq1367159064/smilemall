package cn.smile.smilemall.ware.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-02
 */
@SmileFeign
@FeignClient(value = "cn-smile-smilemall-member")
public interface MemberFeignService {
	
	@RequestMapping("/member/memberreceiveaddress/info/{id}")
	public R info(@PathVariable("id") Long id);
}
