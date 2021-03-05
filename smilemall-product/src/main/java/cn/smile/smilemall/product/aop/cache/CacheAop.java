package cn.smile.smilemall.product.aop.cache;

import cn.smile.smilemall.product.aop.imp.SmileCacheImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-17
 */
@Slf4j
@Aspect
@Component
public class CacheAop {
	
	@Pointcut(value = "execution(* cn.smile.smilemall.product.service.impl.*.*(..))")
	public void pointExpression() {
	}
	
	@Around(value = "pointExpression()")
	public Object around(ProceedingJoinPoint joinPoint) {
		Object returnData = null;
		try {
			returnData = SmileCacheImpl.getSmile().invoking(joinPoint);
		} catch (Exception e) {
			log.error("切方异常 Exception {}", e.getMessage());
		} catch (Throwable throwable) {
			log.error("切方异常 throwable {}", throwable.getMessage());
			throwable.printStackTrace();
		}
		return returnData;
	}
}
