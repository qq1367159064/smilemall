package cn.smile.smilemall.search.services.imp;

import cn.smile.common.to.es.SkuEsModule;
import cn.smile.smilemall.search.config.ElasticSearchConfig;
import cn.smile.smilemall.search.constant.EsConstant;
import cn.smile.smilemall.search.services.ProductServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-15
 */

@Service
@Slf4j
public class ProductServicesImpl implements ProductServices {
	
	@Autowired
	private RestHighLevelClient restHighLevelClient;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * <p>商品上架保存信息到es中</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param skuEsModules 1
	 * @return boolean
	 */
	@Override
	public boolean productStatusUp(List<SkuEsModule> skuEsModules) {
		try {
			BulkRequest bulkRequest = new BulkRequest();
			for (SkuEsModule skuEsModule : skuEsModules) {
				IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
				indexRequest.id(skuEsModule.getSkuId().toString());
				String s = objectMapper.writeValueAsString(skuEsModule);
				indexRequest.source(s, XContentType.JSON);
				bulkRequest.add(indexRequest);
			}
			BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.getCommonOptions());
			log.trace("商品上架 es{}", bulk);
			return !bulk.hasFailures();
		} catch (Exception e) {
			log.error("[product up error] {}", e);
		}
		return false;
	}
}
