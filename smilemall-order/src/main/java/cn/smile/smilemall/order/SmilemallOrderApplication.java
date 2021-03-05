package cn.smile.smilemall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author smile
 */
@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableFeignClients
public class SmilemallOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmilemallOrderApplication.class, args);
	}

}
