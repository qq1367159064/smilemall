package cn.smile.smilemall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Smile
 * @description
 * @date 2021-02-14
 */
@Configuration
public class ElasticSearchConfig {
	
	private static final RequestOptions COMMON_OPTIONS;
	static {
		RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
		COMMON_OPTIONS = builder.build();
	}
	
	public static RequestOptions getCommonOptions() {
		return COMMON_OPTIONS;
	}
	
	@Bean
	public RestHighLevelClient restHighLevelClient() {
		RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.31.239",
				9200, "http")));
		return restHighLevelClient;
	}
}
