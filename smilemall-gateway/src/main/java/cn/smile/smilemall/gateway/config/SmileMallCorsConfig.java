package cn.smile.smilemall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.time.Duration;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/7
 */
@Configuration
public class SmileMallCorsConfig {
	
	@Bean
	public CorsWebFilter corsWebFilter() {
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(Duration.ofHours(1));
		configurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsWebFilter(configurationSource);
	}
}
