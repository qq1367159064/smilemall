package cn.smile.common.smilemall.utils;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-17
 */
public class SmileRedissonTemplate {
	static final RedissonClient MY_REDISSON;
	static {
		MY_REDISSON = MyRedissonTemplate.REDISSON;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(() -> {
				System.out.println(MY_REDISSON);
			});
			thread.start();
		}
	}
	
	
	/**
	 * <p>先加读锁，在排他锁</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param lockName 1
	 * @return void
	 */
	public static void lockReadLock(String lockName) {
		readWriteLock(lockName).readLock().lock();
		getLock(lockName).lock();
	}
	
	/**
	 * <p>释放所有锁</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param lockName 1
	 * @return void
	 */
	public static void unLockReadLock(String lockName) {
		readWriteLock(lockName).readLock().unlock();
		getLock(lockName).unlock();
	}
	
	/**
	 * <p>获取锁</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName 1
	 * @return org.redisson.api.RLock
	 */
	public static RLock getLock(String lockName) {
		return MY_REDISSON.getLock(lockName);
	}
	
	/**
	 * <p>直接加锁, 默认时间 30s</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName 1
	 * @return void
	 */
	public static void lock(String lockName) {
		getLock(lockName).lock();
	}
	
	/**
	 * <p>设置过期时间,单位为毫秒</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName 1
	 * @param laseTime  过期时间
	 * @return void
	 */
	public static void lock(String lockName, Long laseTime) {
		getLock(lockName).lock(laseTime, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * <p>读写锁对象</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName 1
	 * @return org.redisson.api.RReadWriteLock
	 */
	public static RReadWriteLock readWriteLock(String lockName) {
		return MY_REDISSON.getReadWriteLock(lockName);
	}
	
	/**
	 * <p>解锁</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName  锁定名称1
	 * @return void
	 */
	public static void unLock(String lockName) {
		getLock(lockName).unlock();
	}
	
	
	/**
	 * <p>读锁</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName 1
	 * @return void
	 */
	public static void getReadLock(String lockName) {
		readWriteLock(lockName).readLock().lock();
	}
	
	/**
	 * <p>读锁解锁</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName 1
	 * @return void
	 */
	public static void unReadLock(String lockName) {
		readWriteLock(lockName).readLock().unlock();
	}
	
	/**
	 * <p>写锁</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @param lockName 1
	 * @return void
	 */
	public static void getWriteLock(String lockName) {
		readWriteLock(lockName).writeLock().lock();
	}
	
	/**
	 * 写锁解锁
	 * @param lockName
	 */
	public static void unWriteLock(String lockName) {
		readWriteLock(lockName).writeLock().unlock();
	}
	
	public final static class MyRedissonTemplate {
		public final static RedissonClient  REDISSON;
		
		static {
			Config config = new Config();
			config.useSingleServer()
					.setAddress("redis://192.168.31.239:6379")
					.setPassword("smile");
			 REDISSON = Redisson.create(config);
		}
	}
}
