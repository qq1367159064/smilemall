package cn.smile.common.smilemall.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmileMallRedisProperties {
	
	/**
	 * Database index used by the connection factory.
	 */
	private int database = 0;
	
	/**
	 * Connection URL. Overrides host, port, and password. User is ignored. Example:
	 * redis://user:password@example.com:6379
	 */
	private String url;
	
	/**
	 * Redis server host.
	 */
	private String host = "192.168.31.239";
	
	/**
	 * Login password of the redis server.
	 */
	private String password = "smile";
	
	/**
	 * Redis server port.
	 */
	private int port = 6379;
	
	/**
	 * Whether to enable SSL support.
	 */
	private boolean ssl;
	
	/**
	 * Connection timeout.
	 */
	private Duration timeout;
	
	/**
	 * Client name to be set on connections with CLIENT SETNAME.
	 */
	private String clientName;
	
	private Pool pool = new Pool();
	
	
	private Duration shutdownTimeout = Duration.ofMillis(100);
	@Data
	public static class Pool {
		private int maxIdle = 8;
		
		private int minIdle = 0;
		
		private int maxActive = 8;
	
		private Duration maxWait = Duration.ofMillis(-1);

		private Duration timeBetweenEvictionRuns;
	}
	
}
