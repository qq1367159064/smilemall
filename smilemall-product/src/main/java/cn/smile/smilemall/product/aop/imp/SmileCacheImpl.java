package cn.smile.smilemall.product.aop.imp;

import cn.smile.common.annotation.cache.UseCache;
import cn.smile.common.constant.RedisConstant;
import cn.smile.common.smilemall.utils.SmileMallRedisTemplate;
import cn.smile.common.smilemall.utils.SmileRedissonTemplate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-18
 */
@Slf4j
public class SmileCacheImpl implements SmileCache {
	
	private final static String SELECT = "select";
	private final static String UPDATE = "update";
	private final static String INSERT = "insert";
	private final static String DELETE = "delete";
	private final static String LOCK = "lock";
	
	private final static SmileCacheImpl SMILE = new SmileCacheImpl();
	
	private SmileCacheImpl() {};
	
	public static SmileCacheImpl getSmile() {
		return SMILE;
	}
	
	@Override
	public Object select(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		UseCache useCache = method.getAnnotation(UseCache.class);
		String key = getCacheKey(useCache, joinPoint);
		boolean useLock = useCache.useLock();
		Object value = SmileMallRedisTemplate.getValue(key);
		if (value != null) {
			return value;
		}
		if (!useLock) {
			return setCache(joinPoint, key);
		}
		Class aClass = useCache.lockType();
		if (aClass.equals(RLock.class)) {
			try {
				SmileRedissonTemplate.lockReadLock(key + LOCK);
				return setCache(joinPoint, key);
			} finally {
				SmileRedissonTemplate.unLockReadLock(key + LOCK);
			}
		} else if (aClass.equals(RReadWriteLock.class)) {
			try {
				SmileRedissonTemplate.getReadLock(key + LOCK);
				return setCache(joinPoint, key);
			} finally {
				SmileRedissonTemplate.unReadLock(key + LOCK);
			}
		} else {
			return null;
		}
	}
	
	@Override
	public Object insert(ProceedingJoinPoint joinPoint) throws Throwable {
		return update(joinPoint);
	}
	
	@Override
	public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		UseCache useCache = method.getAnnotation(UseCache.class);
		boolean useLock = useCache.useLock();
		String cacheKey = getCacheKey(useCache, joinPoint);
		if (!useLock) {
			return deleteCache(useCache,joinPoint);
		}
		try {
			SmileRedissonTemplate.getWriteLock(cacheKey);
			return deleteCache(useCache, joinPoint);
		} finally {
			SmileRedissonTemplate.unWriteLock(cacheKey);
		}
	}
	
	@Override
	public Object delete(ProceedingJoinPoint joinPoint) throws Throwable {
		return update(joinPoint);
	}
	
	@Override
	public Object invoking(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		UseCache useCache = method.getAnnotation(UseCache.class);
		if (useCache == null || !useCache.useCache()) {
			return joinPoint.proceed(joinPoint.getArgs());
		}
		String operationType = useCache.operationType();
		if (SELECT.equals(operationType)) {
			return select(joinPoint);
		} else if (UPDATE.equals(operationType)) {
			return update(joinPoint);
		} else if (INSERT.equals(operationType)) {
			return insert(joinPoint);
		} else if (DELETE.equals(operationType)) {
			return delete(joinPoint);
		} else {
			return joinPoint.proceed(joinPoint.getArgs());
		}
	}
	
	/**
	 * <p>缓存数据</p>
	 *
	 * @param joinPoint 1
	 * @param key       2
	 * @return java.lang.Object
	 * @author Smile
	 * @date 2021/2/17/017
	 */
	public Object setCache(ProceedingJoinPoint joinPoint, String key) throws Throwable {
		Object value = SmileMallRedisTemplate.getValue(key);
		if (value != null) {
			return value;
		}
		log.debug("查询了数据库{} ", Thread.currentThread().getName());
		Object proceed = joinPoint.proceed(joinPoint.getArgs());
		SmileMallRedisTemplate.setValue(key, proceed, RedisConstant.Lock.UNIT_TIME.getTimeOut());
		return proceed;
	}
	
	/**
	 * <p>删除key</p>
	 *
	 * @param useCache   1
	 * @param joinPoint 2
	 * @return void
	 * @author Smile
	 * @date 2021/2/17/017
	 */
	public Object deleteCache(UseCache useCache, ProceedingJoinPoint joinPoint) throws Throwable {
		Object proceed = joinPoint.proceed(joinPoint.getArgs());
		if (useCache.deleteKey() != null && useCache.deleteKey().length != 0) {
			SmileMallRedisTemplate.deletes(Arrays.stream(useCache.deleteKey()).collect(Collectors.toList()));
		} else {
			SmileMallRedisTemplate.deletes(SmileMallRedisTemplate.keys(useCache.cacheKeyPrefix()));
		}
		return proceed;
	}
	
	/**
	 * <p>获取缓存的key</p>
	 *
	 * @param useCache  1
	 * @param joinPoint 2
	 * @return java.lang.String
	 * @author Smile
	 * @date 2021/2/17/017
	 */
	public String getCacheKey(UseCache useCache, ProceedingJoinPoint joinPoint) {
		StringBuffer cacheKey = new StringBuffer();
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String methodName = method.getName();
		
		String cacheKeyPrefix = useCache.cacheKeyPrefix();
		String cacheKeySuffix = useCache.cacheKeySuffix();
		if (cacheKeyPrefix.equals("")) {
			cacheKey.append(methodName);
		} else {
			cacheKey.append(cacheKeyPrefix).append(":").append(methodName);
		}
		if (cacheKeySuffix.equals("")) {
			cacheKey.append("general");
		} else {
			cacheKey.append(cacheKeySuffix);
		}
		Object[] args = joinPoint.getArgs();
		if (args != null && args.length != 0) {
			try {
				for (Object arg : args) {
					cacheKey.append("-").append(arg);
				}
			} catch (Exception e) {
				log.error("key生成错误 {}, {}" , args, e);
			} finally {
				log.error("缓存key {}", args);
			}
		}
		return cacheKey.toString().trim().replaceAll("\\+s", "");
		
	}
}
