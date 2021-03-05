package cn.smile.smilemall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>redis配置类</p>
 *
 * @author smile
 * @date 2021-02-16
 */
@Configuration
@EnableCaching
public class RedisConfig {
	private static final GenericJackson2JsonRedisSerializer VALUE_SERIALIZER = new GenericJackson2JsonRedisSerializer();
	private static final StringRedisSerializer STRING_REDIS_SERIALIZER  = new StringRedisSerializer();
	
	/**
	 * <p>配置redisTemplate</p>
	 * @author Smile
	 * @date 2021/2/16/016
	 * @param redisConnectionFactory 1
	 * @return org.springframework.data.redis.core.RedisTemplate<java.lang.Object,java.lang.Object>
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setValueSerializer(VALUE_SERIALIZER);
		redisTemplate.setKeySerializer(STRING_REDIS_SERIALIZER);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}
	
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
		CacheProperties.Redis redisProperties = cacheProperties.getRedis();
		org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
				.defaultCacheConfig();
		config = config.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(VALUE_SERIALIZER));
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}
		if (redisProperties.getKeyPrefix() != null) {
			config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
		}
		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}
		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}
		return config;
	}
}
