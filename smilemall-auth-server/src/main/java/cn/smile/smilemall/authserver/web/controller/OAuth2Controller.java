package cn.smile.smilemall.authserver.web.controller;

import cn.hutool.json.JSONUtil;
import cn.smile.common.constant.AuthServerConstant;
import cn.smile.common.exception.BizCodeEnume;
import cn.smile.common.smilemall.utils.HttpUtils;
import cn.smile.common.utils.R;
import cn.smile.smilemall.authserver.feign.MemberFeignService;
import cn.smile.smilemall.authserver.vo.SocialUserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>社交登录Controller</p>
 *
 * @author smile
 * @date 2021-02-23
 */
@Controller
@Slf4j
public class OAuth2Controller {
	
	private final MemberFeignService memberFeignService;
	
	public OAuth2Controller(MemberFeignService memberFeignService) {
		this.memberFeignService = memberFeignService;
	}
	
	@GetMapping("/auth2/success")
	public String authSuccess(@RequestParam("code") String code, HttpSession session) throws JsonProcessingException {
		Map<String, String> postBodyMap = new HashMap<>(10);
		postBodyMap.put("code", code);
		postBodyMap.put("client_id", "4190753467");
		postBodyMap.put("client_secret", "912ca0b10a41680e11dd5206a6bdb2a7");
		postBodyMap.put("grant_type", "authorization_code");
		postBodyMap.put("redirect_uri", "http://auth.smilemall.cn/auth2/success");
		R resultR = null;
		try {
			HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token",
													"POST", new HashMap<>(), postBodyMap, "");
			String socialUserInfo = EntityUtils.toString(response.getEntity());
			SocialUserVo socialUserVo = JSONUtil.toBean(socialUserInfo, SocialUserVo.class);
			resultR = memberFeignService.socialLogin(socialUserVo);
			if(resultR.getCode() == BizCodeEnume.O_AUTH_FAIL_EXCEPTION.getCode()) {
				return "redirect:http://auth.smilemall.cn/login.html";
			}
		} catch (Exception e) {
			log.error("获取access_token异常 {}", e.getMessage());
			return "redirect:http://auth.smilemall.cn/login.html";
		}
		session.setAttribute(AuthServerConstant.LONG_USER_INFO, resultR.get("userInfo"));
		return "redirect:http://smilemall.cn";
	}
}
