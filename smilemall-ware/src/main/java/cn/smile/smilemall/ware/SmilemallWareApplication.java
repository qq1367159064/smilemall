package cn.smile.smilemall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAspectJAutoProxy
public class SmilemallWareApplication {
	
	public static void main(String[] args) {
		try {
			SpringApplication.run(SmilemallWareApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
