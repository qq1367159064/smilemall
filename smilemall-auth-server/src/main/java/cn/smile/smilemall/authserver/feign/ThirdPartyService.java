package cn.smile.smilemall.authserver.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-23
 */
@FeignClient(value = "cn-smile-smilemall-third-party")
@SmileFeign
public interface ThirdPartyService {
	
	/**
	 * <p>获取验证码</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param phone 1
	 * @return cn.smile.common.utils.R
	 */
	@GetMapping("/sms/sendCodeAutoCreateCode")
	public R sendNote(@RequestParam(value = "phone") String phone) ;
}
