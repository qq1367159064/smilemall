package cn.smile.smilemall.authserver.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import cn.smile.smilemall.authserver.vo.SocialUserVo;
import cn.smile.smilemall.authserver.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>用户服务调用</p>
 *
 * @author smile
 * @date 2021-02-23
 */
@SmileFeign
@FeignClient(value = "cn-smile-smilemall-member")
public interface MemberFeignService {
	
	/**
	 * <p>用户注册</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param registerVo 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/member/member/register")
	public R register(@RequestBody UserRegisterVo registerVo) ;
	
	/**
	 * <p>用户登录</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param userRegisterVo 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/member/member/userLogin")
	public R userLogin(@RequestBody UserRegisterVo userRegisterVo) ;
	
	/**
	 * <p>社交登录</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param socialUserVo 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/member/member/socialLogin")
	public R socialLogin(@RequestBody SocialUserVo socialUserVo);
}
