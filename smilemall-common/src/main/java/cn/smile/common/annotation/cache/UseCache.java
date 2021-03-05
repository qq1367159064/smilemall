package cn.smile.common.annotation.cache;

import org.redisson.api.RReadWriteLock;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * <p>表示是否需要使用缓存</p>
 *
 * @author smile
 * @date 2021-02-17
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface UseCache {

	/**
	 * <p>指定缓存key,指定key</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @return java.lang.String
	 */
	String assignKey() default "";
	
	/**
	 * <p>指定锁的名称</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @return java.lang.String
	 */
	String lockKey() default "";
	
	 /**
	 * <p>缓存的key的前缀</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @return java.lang.String
	 */
	 String cacheKeyPrefix() default "";
	 
	 /**
	  * <p>指定清空缓存的key</p>
	  * @author Smile
	  * @date 2021/2/17/017
	  * @return java.lang.String[]
	  */
	 String[] deleteKey() default {};
	 
	 
	 /**
	  * <p>缓存key的后缀</p>
	  * @author Smile
	  * @date 2021/2/17/017
	  * @return java.lang.String
	  */
	 String cacheKeySuffix() default "";
	 /**
	  * <p>是否使用缓存</p>
	  * @author Smile
	  * @date 2021/2/17/017
	  * @return boolean
	  */
	 boolean useCache() default true;
	 /**
	  * <p>是否加锁</p>
	  * @author Smile
	  * @date 2021/2/17/017
	  * @return boolean
	  */
	 boolean useLock() default false;
	 
	 /**
	  * <p>加锁的类型</p>
	  * @author Smile
	  * @date 2021/2/17/017
	  * @return java.lang.Class<?>
	  */
	 Class lockType() default RReadWriteLock.class ;
	 
	 /**
	  * <p>缓存的失效时间，单位毫秒</p>
	  * @author Smile
	  * @date 2021/2/17/017
	  * @return long
	  */
	 long timeout() default 30L;
	 
	 /**
	  * <p>操作类型  only { update, insert, select, delete}</p>
	  * @author Smile
	  * @date 2021/2/17/017
	  * @return java.lang.String
	  */
	 String operationType() default "select";
}
