package cn.smile.smilemall.search;

import cn.smile.smilemall.search.config.ElasticSearchConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@Slf4j
class SmilemallSearchApplicationTests {
	
	@Autowired
	private RestHighLevelClient restHighLevelClient;
	
	
	/**
	 * @description 保存索引
	 * @author Smile
	 * @date 2021/2/14/014
	 * @param
	 * @return void
	 */
	@Test
	public void indexData() throws Exception {
		IndexRequest indexRequest = new IndexRequest("person");
		ObjectMapper objectMapper = new ObjectMapper();
		String person = objectMapper.writeValueAsString(new Person("张三", 18, "男"));
		indexRequest.source(person, XContentType.JSON);
		IndexResponse index = restHighLevelClient.index(indexRequest, ElasticSearchConfig.getCommonOptions());
		log.trace("index", index);
	}
	
	@Test
	public void searchData() throws IOException {
		// 搜索请求
		SearchRequest searchRequest = new SearchRequest("person");
		// 请求条件构造
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		// 指定检索条件
		searchRequest.source(searchSourceBuilder);
		SearchResponse search = restHighLevelClient.search(searchRequest, ElasticSearchConfig.getCommonOptions());
		log.trace("search", search);
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class Person {
		private String name;
		private Integer age;
		private String gender;
	}
	
	
	
	@Test
	void contextLoads() {
		System.out.println(restHighLevelClient);
	}
	
}
