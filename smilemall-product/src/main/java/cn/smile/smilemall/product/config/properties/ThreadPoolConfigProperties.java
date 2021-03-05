package cn.smile.smilemall.product.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-22
 */
@ConfigurationProperties(prefix = "smilemall.thread")
@Data
public class ThreadPoolConfigProperties {
	
	private Integer coreSize;
	private Integer maxSize;
	private Integer keepAliveTime;
}
