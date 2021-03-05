package cn.smile.smilemall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * <p>redissonConfig配置类</p>
 *
 * @author smile
 * @date 2021-02-17
 */
@Configuration
public class MyRedissonConfig {
	
	/**
	 * <p>配置redisson</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @return org.redisson.api.RedissonClient
	 */
	@Bean(destroyMethod="shutdown")
	RedissonClient redisson() throws IOException {
		Config config = new Config();
		config.useSingleServer()
				.setAddress("redis://192.168.31.239:6379")
		        .setPassword("smile");
		return Redisson.create(config);
	}

}
