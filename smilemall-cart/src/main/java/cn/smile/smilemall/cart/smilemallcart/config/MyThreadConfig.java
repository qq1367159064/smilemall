package cn.smile.smilemall.cart.smilemallcart.config;

import cn.smile.smilemall.cart.smilemallcart.config.properties.ThreadPoolConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>线程池</p>
 *
 * @author smile
 * @date 2021-02-22
 */
@Configuration
@EnableConfigurationProperties(value = ThreadPoolConfigProperties.class)
public class MyThreadConfig {
	
	@Bean
	public ThreadPoolExecutor threadPoolExecutor() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				20,
				200,
				10,
				TimeUnit.SECONDS,
				new LinkedBlockingDeque<>(100000),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy());
		return threadPoolExecutor;
	}
}
