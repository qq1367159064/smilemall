package cn.smile.smilemall.cart.smilemallcart.config;

import cn.smile.smilemall.cart.smilemallcart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-25
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration cartInterceptor = registry.addInterceptor(new CartInterceptor());
		cartInterceptor.addPathPatterns("/**");
	}
}
