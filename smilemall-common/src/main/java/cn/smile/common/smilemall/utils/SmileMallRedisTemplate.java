package cn.smile.common.smilemall.utils;

import cn.smile.common.smilemall.properties.SmileMallRedisProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.TimeoutOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-16
 */
public class SmileMallRedisTemplate{
	private static final RedisTemplate MY_REDIS_TEMPLATE;
	
	static {
		MY_REDIS_TEMPLATE = SmileRedisTemplate.redisTemplate;
	}
	
	
	/*=================================================opsForHash===========================================*/
	
	/**
	 * <p>根据某个key绑定一个hash操作</p>
	 * @author smile
	 * @date 2021/2/26/026
	 * @param key 1
	 * @return org.springframework.data.redis.core.BoundHashOperations
	 */
	public static BoundHashOperations boundHashOperations(String key) {
		return MY_REDIS_TEMPLATE.boundHashOps(key);
	}
	
	
	/*=================================================opsForValue=========================================*/
	/**
	 * <p>没有设置过期时间的值保存</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param key 1
	 * @param value 2
	 * @return void
	 */
	public static  void setValue(String key, Object value) {
		SmileRedisTemplate.redisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * <p>批量删除</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param keys 1
	 * @return void
	 */
	public static <K> void deletes(Collection<K> keys) {
		MY_REDIS_TEMPLATE.delete(keys);
	}
	
	/**
	 * <p>设置过期时间的值保存，单位为毫秒</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param kye 1
	 * @param value 2
	 * @param timeOut 3
	 * @return void
	 */
	public static  void setValue(String kye, Object value, Long timeOut) {
		MY_REDIS_TEMPLATE.opsForValue().set(kye, value, timeOut, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * <p>如果redis中不存在值就插入</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param key 1
	 * @param value 2
	 * @param timeout 3
	 * @return java.lang.Boolean
	 */
	public static  Boolean setIfAbsent(String key, Object value, Long timeout) {
		return MY_REDIS_TEMPLATE.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * <p>删除key</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param key 1
	 * @return void
	 */
	public static void delete(String key) {
		MY_REDIS_TEMPLATE.delete(key);
	}
	
	
	public static Boolean  execute(String script, String key, String value) {
		if(StringUtils.isEmpty(script)) {
			// lua脚本
			script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
		}
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script,Long.class);
		
		Long execute = (Long) MY_REDIS_TEMPLATE.execute(redisScript, Arrays.asList(key), value);
		return execute != 0L;
	}
	
	/**
	 * <p>设置key的过期时间单位毫秒</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param key 1
	 * @param timout 2
	 * @return void
	 */
	public static void expire(String key, Long timout) {
		MY_REDIS_TEMPLATE.expire(key, timout, TimeUnit.MILLISECONDS);
	}
	
	
	/**
	 * <p>获取指定前缀的所有key</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param keyPrefix 1
	 * @return java.util.Collection
	 */
	public static Collection keys(String keyPrefix) {
		return MY_REDIS_TEMPLATE.keys(keyPrefix + "*");
	}
	
	/**
	 * <p>根据key返回值</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param key 1
	 * @return Object
	 */
	public static Object getValue(Object key) {
		Object o = MY_REDIS_TEMPLATE.opsForValue().get(key);
		return  o;
	}
	
	public static void main(String[] args) {
		setValue("kye", "测试");
	}
	
	/**
	 * <p>redis模板初始化</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @return
	 */
	private static class SmileRedisTemplate {
		private static final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		static {
			redisTemplate.setKeySerializer(RedisTemplateConfig.STRING_REDIS_SERIALIZER);
			redisTemplate.setValueSerializer(RedisTemplateConfig.VALUE_SERIALIZER);
			redisTemplate.setConnectionFactory(RedisTemplateConfig.redisConnectionFactory());
			redisTemplate.setHashKeySerializer(RedisTemplateConfig.STRING_REDIS_SERIALIZER);
			redisTemplate.setHashValueSerializer(RedisTemplateConfig.VALUE_SERIALIZER);
			redisTemplate.setEnableDefaultSerializer(false);
			redisTemplate.afterPropertiesSet();
		}
	}
	
	static class RedisTemplateConfig {
		private static final GenericJackson2JsonRedisSerializer VALUE_SERIALIZER = new GenericJackson2JsonRedisSerializer();
		private static final StringRedisSerializer STRING_REDIS_SERIALIZER  = new StringRedisSerializer();
		
		public static LettuceConnectionFactory redisConnectionFactory() {
			SmileMallRedisProperties redisProperties = new SmileMallRedisProperties();
			SmileMallRedisProperties.Pool pool = redisProperties.getPool();
			pool.setMaxActive(20);
			// 获取Redis配置
			RedisConfiguration redisConfiguration = build(redisProperties);
			//获取连接配置
			LettuceClientConfiguration lettuceClientConfiguration = getLettuceClientConfiguration(redisProperties);
			LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
			lettuceConnectionFactory.afterPropertiesSet();
			return lettuceConnectionFactory;
		}
		
		// 构建redis配置
		private static RedisConfiguration build(SmileMallRedisProperties redisProperties) {
			RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
			redisConfiguration.setDatabase(redisProperties.getDatabase());
			redisConfiguration.setHostName(redisProperties.getHost());
			redisConfiguration.setPort(redisProperties.getPort());
			redisConfiguration.setDatabase(redisProperties.getDatabase());
			redisConfiguration.setPassword(redisProperties.getPassword());
			return redisConfiguration;
		}
		
		// 获取连接配置
		private static LettuceClientConfiguration getLettuceClientConfiguration(SmileMallRedisProperties redisProperties) {
			LettuceClientConfiguration.LettuceClientConfigurationBuilder build = createBuild(redisProperties.getPool());
			build.shutdownTimeout(redisProperties.getShutdownTimeout());
			build.clientOptions(ClientOptions.builder().timeoutOptions(TimeoutOptions.enabled()).build());
			return build.build();
		}
		
		// 获取构建工厂
		private  static LettuceClientConfiguration.LettuceClientConfigurationBuilder createBuild(SmileMallRedisProperties.Pool pool) {
			return LettucePoolingClientConfiguration.builder().poolConfig(getPoolConfig(pool));
		}
		
		// 配置连接池
		private static GenericObjectPoolConfig<?> getPoolConfig(SmileMallRedisProperties.Pool properties) {
			GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
			config.setMaxTotal(properties.getMaxActive());
			config.setMaxIdle(properties.getMaxIdle());
			config.setMinIdle(properties.getMinIdle());
			if (properties.getTimeBetweenEvictionRuns() != null) {
				config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
			}
			if (properties.getMaxWait() != null) {
				config.setMaxWaitMillis(properties.getMaxWait().toMillis());
			}
			return config;
		}
		
	}
}
