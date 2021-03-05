package cn.smile.smilemall.order.interceptor;

import cn.smile.common.constant.AuthServerConstant;
import cn.smile.common.vo.LoginUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-28
 */
public class UserLoginInterceptor implements HandlerInterceptor {
	
	public static final ThreadLocal<LoginUserInfo> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		LoginUserInfo userInfo ;
		Object attribute = session.getAttribute(AuthServerConstant.LONG_USER_INFO);
		if(attribute == null) {
			response.sendRedirect("http://auth.smilemall.cn/login.html");
			return false;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		userInfo = objectMapper.readValue(objectMapper.writeValueAsString(attribute), LoginUserInfo.class);
		USER_INFO_THREAD_LOCAL.set(userInfo);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		USER_INFO_THREAD_LOCAL.remove();
	}
}
