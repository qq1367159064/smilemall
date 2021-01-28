package cn.smile.smilemall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description
 * @author Smile
 * @date 2021/1/24/024
 * @return
 */

@EnableFeignClients
@SpringBootApplication
@MapperScan(value = { "cn.smile.smilemall.product.dao"})
public class SmilemallProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmilemallProductApplication.class, args);
	}

}
