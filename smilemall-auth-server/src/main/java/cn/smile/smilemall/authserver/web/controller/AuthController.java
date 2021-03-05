package cn.smile.smilemall.authserver.web.controller;

import cn.hutool.json.JSONUtil;
import cn.smile.common.constant.AuthServerConstant;
import cn.smile.common.exception.BizCodeEnume;
import cn.smile.common.smilemall.utils.SmileMallRedisTemplate;
import cn.smile.common.utils.R;
import cn.smile.common.vo.LoginUserInfo;
import cn.smile.smilemall.authserver.feign.MemberFeignService;
import cn.smile.smilemall.authserver.feign.ThirdPartyService;
import cn.smile.smilemall.authserver.vo.UserRegisterVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-23
 */
@Controller
public class AuthController {
	
	private final ThirdPartyService thirdPartyService;
	private final MemberFeignService memberFeignService;
	
	
	public AuthController(ThirdPartyService thirdPartyService, MemberFeignService memberFeignService) {
		this.thirdPartyService = thirdPartyService;
		this.memberFeignService = memberFeignService;
	}
	
	@GetMapping("/login.html")
	public String loginPage(HttpSession session) {
		if (session.getAttribute(AuthServerConstant.LONG_USER_INFO) != null) {
			return "redirect:http://smilemall.cn";
		}
		return "login";
	}
	
	@ResponseBody
	@GetMapping("/auth/sendNote")
	public R sendNode(String phone) {
		return thirdPartyService.sendNote(phone);
	}
	
	@PostMapping("/register")
	public String register(@Validated UserRegisterVo userRegisterVo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errMsg =
					bindingResult.getFieldErrors().stream().collect(Collectors.toMap(key -> key.getField(),
							value -> value.getDefaultMessage()));
			redirectAttributes.addFlashAttribute("errMsg", errMsg);
			return "redirect:http://auth.smilemall.cn/reg.html";
		}
		String code = userRegisterVo.getCode();
		String exitCode = (String) SmileMallRedisTemplate.getValue(userRegisterVo.getPhone());
		if (StringUtils.isEmpty(exitCode)) {
			return commonErrMsgManage(redirectAttributes, "验证码无效", "code");
		}
		String splitSymbol = "_";
		if (!exitCode.split(splitSymbol)[0].equals(code)) {
			return commonErrMsgManage(redirectAttributes, "验证码不正确", "code");
		}
		try {
			R registerResult = memberFeignService.register(userRegisterVo);
			if (registerResult.getCode() == BizCodeEnume.PHONE_EXIT_EXCEPTION.getCode()) {
				return commonErrMsgManage(redirectAttributes, registerResult.get("msg").toString(), "phone");
			}
			if (registerResult.getCode() == BizCodeEnume.USER_EXIT_EXCEPTION.getCode()) {
				return commonErrMsgManage(redirectAttributes, registerResult.get("msg").toString(), "username");
			}
			if (registerResult.getCode() == BizCodeEnume.UNKNOW_EXCEPTION.getCode()) {
				return commonErrMsgManage(redirectAttributes, registerResult.get("msg").toString(), "common");
			}
		} catch (Exception e) {
			return commonErrMsgManage(redirectAttributes, "注册失败", "common");
		} finally {
			SmileMallRedisTemplate.delete(userRegisterVo.getPhone());
		}
		return "redirect:http://auth.smilemall.cn/login.html";
	}
	
	@PostMapping("/userLogin")
	public String userLogin(UserRegisterVo userRegisterVo, HttpSession session, RedirectAttributes redirectAttributes) {
		Map<String, String> error = new HashMap<>(10);
		R u;
		try {
			u = memberFeignService.userLogin(userRegisterVo);
			if (u.getCode() == BizCodeEnume.ACCOUNT_ERROR.getCode()) {
				error.put("msg", u.get("msg").toString());
				redirectAttributes.addFlashAttribute("error", error);
				return "redirect:http://auth.smilemall.cn/login.html";
			}
			if(u.getCode() == BizCodeEnume.PASSWORD_ERROR.getCode()) {
				error.put("msg", u.get("msg").toString());
				redirectAttributes.addFlashAttribute("error", error);
				return "redirect:http://auth.smilemall.cn/login.html";
			}
		} catch (Exception e) {
			error.put("msg", e.getMessage());
			redirectAttributes.addFlashAttribute("error", error);
			return "redirect:http://auth.smilemall.cn/login.html";
		}
		session.setAttribute(AuthServerConstant.LONG_USER_INFO, u.get("userInfo"));
		return "redirect:http://smilemall.cn";
	}
	
	public String commonErrMsgManage(RedirectAttributes redirectAttributes, String msg, String key) {
		Map<String, String> errMsg = new HashMap<>(10);
		errMsg.put(key, msg);
		redirectAttributes.addFlashAttribute("errMsg", errMsg);
		return "redirect:http://auth.smilemall.cn/reg.html";
	}
}
