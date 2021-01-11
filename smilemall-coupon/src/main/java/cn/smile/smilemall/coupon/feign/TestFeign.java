package cn.smile.smilemall.coupon.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/7
 */
@FeignClient(value = "cn-smile-smilemall-member")
@SmileFeign
public interface TestFeign {
	@GetMapping("/member/test/getTest")
	public R getTest() ;
}
