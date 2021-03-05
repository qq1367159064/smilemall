package cn.smile.smilemall.order.config;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-05
 */

@Configuration
public class MySeataConfig {
	
	@Autowired
	public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}
	
	private DataSourceProperties dataSourceProperties;

	
	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties) {
		HikariDataSource source = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
		
		if(StringUtils.hasText(dataSourceProperties.getName())) {
			source.setPoolName(dataSourceProperties.getName());
		}
		return new DataSourceProxy(source);
	}
}
