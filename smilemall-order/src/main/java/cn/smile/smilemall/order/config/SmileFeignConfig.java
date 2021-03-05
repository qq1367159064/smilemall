package cn.smile.smilemall.order.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-28
 */
@Configuration
public class SmileFeignConfig {
	
	@Bean
	public RequestInterceptor requestInterceptor() {
		RequestInterceptor requestInterceptor = (template) -> {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String cookie = request.getHeader("Cookie");
			template.header("Cookie", cookie);
		};
		return requestInterceptor;
	}
}
