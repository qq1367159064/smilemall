package cn.smile.smilemall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Smile
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class SmilemallThirdPartyApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SmilemallThirdPartyApplication.class, args);
	}
	
}
