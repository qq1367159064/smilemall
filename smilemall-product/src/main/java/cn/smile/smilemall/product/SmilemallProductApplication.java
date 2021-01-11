package cn.smile.smilemall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = { "cn.smile.smilemall.product.dao"})
public class SmilemallProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmilemallProductApplication.class, args);
	}

}
