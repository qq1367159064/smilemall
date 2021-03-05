package cn.smile.smilemall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Smile
 * @date 2020/2/14
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableDiscoveryClient
@EnableFeignClients
public class SmilemallSearchApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmilemallSearchApplication.class, args);
	}
}
