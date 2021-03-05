package cn.smile.smilemall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-24
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
	
	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
		defaultCookieSerializer.setCookieName("SMILEMALL_SESSION");
		defaultCookieSerializer.setCookiePath("/");
		defaultCookieSerializer.setDomainName("smilemall.cn");
		return defaultCookieSerializer;
	}
	
	@Bean
	public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
		return new GenericJackson2JsonRedisSerializer();
	}
	
}
