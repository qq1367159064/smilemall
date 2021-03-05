package cn.smile.smilemall.ware.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/28/028
 */
@Configuration
@EnableTransactionManagement
@MapperScan(value = { "cn.smile.smilemall.ware.dao" })
public class MybatisConfig {
	/**
	 * @Description 配置{ MybatisPlus }分类插件
	 * @author Smile
	 * @date 2021/1/15/015
	 * @param
	 * @return com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setOverflow(true);
		paginationInterceptor.setLimit(1000);
		return paginationInterceptor;
	}
}
