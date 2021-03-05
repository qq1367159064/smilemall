package cn.smile.smilemall.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-23
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/reg.html").setViewName("reg");
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
