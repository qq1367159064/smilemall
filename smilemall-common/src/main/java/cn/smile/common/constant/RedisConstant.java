package cn.smile.common.constant;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-16
 */
public class RedisConstant {
	
	public static enum Lock {
		TIME_OUT(3000L),
		SLEEP_TIME(300L),
		UNIT_TIME(300000L);
		private Long timeOut;
		Lock(Long timeOut) {
			this.timeOut  = timeOut;
		}
		public Long getTimeOut() {
			return timeOut;
		}
		public void setTimeOut(Long timeOut) {
			this.timeOut = timeOut;
		}
	}
}
