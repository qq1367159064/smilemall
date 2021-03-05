package cn.smile.smilemall.product.aop.imp;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-18
 */
public interface SmileCache {
	
	/**
	 * <p>自定义数据查询的时候缓存操作</p>
	 * @exception
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param joinPoint 1
	 * @return java.lang.Object
	 */
	public Object select(ProceedingJoinPoint joinPoint) throws Throwable;
	
	/**
	 * <p>自定义数据添加的时候缓存操作</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param joinPoint 1
	 * @return java.lang.Object
	 */
	public Object insert(ProceedingJoinPoint joinPoint) throws Throwable;
	
	/**
	 * <p>自定义数据update的时候缓存操作</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param joinPoint 1
	 * @return java.lang.Object
	 */
	public Object update(ProceedingJoinPoint joinPoint) throws Throwable;
	
	/**
	 * <p>自定义数据delete的时候缓存操作</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param joinPoint 1
	 * @return java.lang.Object
	 */
	public Object delete(ProceedingJoinPoint joinPoint) throws Throwable;
	
	/**
	 * <p></p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param joinPoint 1
	 * @return java.lang.Object
	 */
	public Object invoking(ProceedingJoinPoint joinPoint) throws Throwable;
}
