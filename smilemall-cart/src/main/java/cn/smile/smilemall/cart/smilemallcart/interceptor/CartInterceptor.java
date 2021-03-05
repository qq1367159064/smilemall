package cn.smile.smilemall.cart.smilemallcart.interceptor;

import cn.smile.common.constant.AuthServerConstant;
import cn.smile.smilemall.cart.smilemallcart.constant.CartConstant;
import cn.smile.smilemall.cart.smilemallcart.to.UserInfoTo;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-25
 */
public class CartInterceptor implements HandlerInterceptor {
	
	public final static String REQUEST_USERINFO = "userInfoTo";
	public final static boolean HAS_COOKIE = false;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		UserInfoTo userInfoTo = new UserInfoTo();
		Map member = (Map) session.getAttribute(AuthServerConstant.LONG_USER_INFO);
		if (member != null) {
			userInfoTo.setUserId(member.get("id").toString());
		}
		request.setAttribute("HAS_COOKIE", HAS_COOKIE);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length != 0) {
			for (Cookie cookie : cookies) {
				String ckName = cookie.getName();
				if (CartConstant.COOKIE_NAME.equals(ckName)) {
					request.setAttribute("HAS_COOKIE", true);
					userInfoTo.setUserKey(ckName);
				}
			}
		}
		
		if (StringUtils.isEmpty(userInfoTo.getUserKey())) {
			userInfoTo.setUserKey(UUID.randomUUID().toString());
		}
		request.setAttribute(REQUEST_USERINFO, userInfoTo);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		UserInfoTo userInfo = (UserInfoTo) request.getAttribute(REQUEST_USERINFO);
		boolean hasCookie = (boolean) request.getAttribute("HAS_COOKIE");
		if (!hasCookie) {
			Cookie cookie = new Cookie(CartConstant.COOKIE_NAME, userInfo.getUserKey());
			cookie.setPath("/");
			cookie.setDomain("smilemall.cn");
			cookie.setMaxAge(CartConstant.COOKIE_MAX_AGE);
			response.addCookie(cookie);
		}
		request.removeAttribute("HAS_COOKIE");
		request.removeAttribute(REQUEST_USERINFO);
	}
}
