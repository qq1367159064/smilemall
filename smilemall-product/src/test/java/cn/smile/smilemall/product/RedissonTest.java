package cn.smile.smilemall.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-17
 */
@Slf4j
@SpringBootTest
public class RedissonTest {
	
	/**
	 * <p>Redisson实现了锁的自动续期</p>
	 * @author Smile
	 * @date 2021/2/17/017
	 * @return
	 */
	@Autowired
	private RedissonClient redisson;
	@Test
	void redissonTest() {
	
	}
	
	public void functionA() {
		RLock lock = redisson.getLock("my-lock");
		lock.lock();
		functionB();
		lock.unlock();
		log.info("方法a");
	}
	public void functionB() {
		log.info("方法b");
	}
}
