package cn.smile.smilemall.search;

import cn.smile.smilemall.search.config.ElasticSearchConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Smile
 * @description
 * @date 2021-02-14
 */
@Slf4j
public class ElasticSearchTest {
	
	
	private RestHighLevelClient restHighLevelClient;
	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void beforeAll() {
		ElasticSearchConfig elasticSearchConfig = new ElasticSearchConfig();
		restHighLevelClient  = elasticSearchConfig.restHighLevelClient();
	}
	
	
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
		SearchRequest searchRequest = new SearchRequest("bank");
		// 请求条件构造
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 构建查询条件
		searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
		// 聚合操作
		searchSourceBuilder.aggregation(
				AggregationBuilders.terms("ageTerms").field("age")
						.subAggregation(AggregationBuilders.avg("ageAvg").field("age")));
		searchSourceBuilder.aggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
		// 指定检索条件
		searchRequest.source(searchSourceBuilder);
		SearchResponse search = restHighLevelClient.search(searchRequest, ElasticSearchConfig.getCommonOptions());
		SearchHit[] hits = search.getHits().getHits();
		Arrays.stream(hits).forEach(item -> {
			try {
				Account account = objectMapper.readValue(item.getSourceAsString(), Account.class);
				log.info(account.toString());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}
	
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class Person {
		private String name;
		private Integer age;
		private String gender;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	static class Account {
		private int account_number;
		private int balance;
		private String firstname;
		private String lastname;
		private int age;
		private String gender;
		private String address;
		private String employer;
		private String email;
		private String city;
		private String state;
		public void setAccount_number(int account_number) {
			this.account_number = account_number;
		}
		public int getAccount_number() {
			return account_number;
		}
		
		public void setBalance(int balance) {
			this.balance = balance;
		}
		public int getBalance() {
			return balance;
		}
		
		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
		public String getFirstname() {
			return firstname;
		}
		
		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
		public String getLastname() {
			return lastname;
		}
		
		public void setAge(int age) {
			this.age = age;
		}
		public int getAge() {
			return age;
		}
		
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getGender() {
			return gender;
		}
		
		public void setAddress(String address) {
			this.address = address;
		}
		public String getAddress() {
			return address;
		}
		
		public void setEmployer(String employer) {
			this.employer = employer;
		}
		public String getEmployer() {
			return employer;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		public String getEmail() {
			return email;
		}
		
		public void setCity(String city) {
			this.city = city;
		}
		public String getCity() {
			return city;
		}
		
		public void setState(String state) {
			this.state = state;
		}
		public String getState() {
			return state;
		}
		
	}
}
