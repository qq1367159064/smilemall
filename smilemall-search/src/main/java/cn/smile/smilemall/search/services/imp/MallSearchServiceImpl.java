package cn.smile.smilemall.search.services.imp;

import cn.hutool.json.JSONUtil;
import cn.smile.common.to.es.SkuEsModule;
import cn.smile.common.utils.R;
import cn.smile.smilemall.search.config.ElasticSearchConfig;
import cn.smile.smilemall.search.constant.EsConstant;
import cn.smile.smilemall.search.feign.ProductFeignService;
import cn.smile.smilemall.search.services.MallSearchService;
import cn.smile.smilemall.search.vo.SearchParamVo;
import cn.smile.smilemall.search.vo.SearchResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-18
 */
@Service
@Slf4j
public class MallSearchServiceImpl implements MallSearchService {
	
	
	private static final String ATTR_SPLIT_SYMBOL = "_";
	
	private final RestHighLevelClient restHighLevelClient;
	private final ProductFeignService productFeignService;
	
	public MallSearchServiceImpl(RestHighLevelClient restHighLevelClient, ProductFeignService productFeignService) {
		this.restHighLevelClient = restHighLevelClient;
		this.productFeignService = productFeignService;
	}
	
	@Override
	public SearchResponseVo search(SearchParamVo searchParamVo) {
		SearchRequest searchRequest = buildSearchRequest(searchParamVo);
		SearchResponseVo searchResponseVo = null;
		try {
			SearchResponse search = restHighLevelClient.search(searchRequest, ElasticSearchConfig.getCommonOptions());
			searchResponseVo = buildResult(search, searchParamVo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return searchResponseVo;
	}
	
	/**
	 * <p>返回结构构建 </p>
	 *
	 * @param searchResponse 1
	 * @return cn.smile.smilemall.search.vo.SearchResponseVo
	 * @author smile
	 * @date 2021/2/19/019
	 */
	private SearchResponseVo buildResult(SearchResponse searchResponse, SearchParamVo paramVo) {
		SearchResponseVo searchResponseVo = new SearchResponseVo();
		
		SearchHits parentHits = searchResponse.getHits();
		long total = parentHits.getTotalHits().value;
		searchResponseVo.setTotal(total);
		
		long totalPage = ((total + EsConstant.PAGE_SIZE) - 1) / EsConstant.PAGE_SIZE;
		searchResponseVo.setPageNum(paramVo.getPageNum());
		searchResponseVo.setTotalPages((int) totalPage);
		
		SearchHit[] subHits = parentHits.getHits();
		List<SkuEsModule> skuEsModules = new ArrayList<>();
		if (subHits != null && subHits.length != 0) {
			for (SearchHit subHit : subHits) {
				HighlightField skuTitle = subHit.getHighlightFields().get("skuTitle");
				String sourceJson = subHit.getSourceAsString();
				SkuEsModule skuEsModule = JSONUtil.toBean(sourceJson, SkuEsModule.class);
				if (skuTitle != null) {
					String highSkuTitle = skuTitle.getFragments()[0].string();
					skuEsModule.setSkuTitle(highSkuTitle);
				}
				skuEsModules.add(skuEsModule);
			}
		}
		searchResponseVo.setSkuEsModules(skuEsModules);
		
		ParsedLongTerms catalogAgg = searchResponse.getAggregations().get("catalogAgg");
		List<SearchResponseVo.CategoryVo> categoryVos = new ArrayList<>();
		for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
			SearchResponseVo.CategoryVo categoryVo = new SearchResponseVo.CategoryVo();
			categoryVo.setCatalogId(Long.parseLong(bucket.getKeyAsString()));
			ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalogName");
			String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
			categoryVo.setCatalogName(catalogName);
			categoryVos.add(categoryVo);
		}
		searchResponseVo.setCategoryVos(categoryVos);
		
		List<SearchResponseVo.BrandVo> brandVos = new ArrayList<>();
		ParsedLongTerms brandAgg = searchResponse.getAggregations().get("brandAgg");
		for (Terms.Bucket bucket : brandAgg.getBuckets()) {
			SearchResponseVo.BrandVo brandVo = new SearchResponseVo.BrandVo();
			brandVo.setBrandId(bucket.getKeyAsNumber().longValue());
			
			ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brandNameAgg");
			String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
			brandVo.setBrandName(brandName);
			
			ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brandImgAgg");
			String brandImgName = brandImgAgg.getBuckets().get(0).getKeyAsString();
			brandVo.setBrandImgName(brandImgName);
			
			brandVos.add(brandVo);
		}
		searchResponseVo.setBrandVos(brandVos);
		
		ParsedNested attrAgg = searchResponse.getAggregations().get("attrAgg");
		ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
		List<SearchResponseVo.AttrVo> attrVos = new ArrayList<>();
		for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
			SearchResponseVo.AttrVo attrVo = new SearchResponseVo.AttrVo();
			attrVo.setAttrId(bucket.getKeyAsNumber().longValue());
			
			ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
			attrVo.setAttrName(attrNameAgg.getBuckets().get(0).getKeyAsString());
			
			ParsedStringTerms attrValuesAgg = bucket.getAggregations().get("attrValuesAgg");
			attrVo.setAttrValues(attrValuesAgg.getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList()));
			
			attrVos.add(attrVo);
		}
		searchResponseVo.setAttrVos(attrVos);
		
		if (paramVo.getAttrs() != null && paramVo.getAttrs().size() != 0) {
			List<SearchResponseVo.NavsVo> navsVoList = paramVo.getAttrs().stream().map(attr -> {
				SearchResponseVo.NavsVo vo = new SearchResponseVo.NavsVo();
				String[] voAttrValues = attr.split(ATTR_SPLIT_SYMBOL);
				searchResponseVo.getAttrIds().add(Long.parseLong(voAttrValues[0]));
				R info = productFeignService.info(Long.parseLong(voAttrValues[0]));
				if (info.getCode() == 0) {
					Map attrMap = (Map) info.get("attr");
					vo.setNavName(attrMap.get("attrName").toString());
				} else {
					vo.setNavName(voAttrValues[0]);
				}
				vo.setNavValue(voAttrValues[1]);
				String queryString = paramVo.getQueryString();
				try {
					String e = URLEncoder.encode(attr, "utf-8");
					e = e.replace("+", "%20");
					vo.setLink(
							"http://search.smilemall.cn/list.html?" +
									queryString.replaceAll("&attrs=" + e, "")
											.replaceAll("\\?attrs=" + e, "").replaceAll("attrs=" + e, ""));
				} catch (UnsupportedEncodingException unsupportedEncodingException) {
					unsupportedEncodingException.printStackTrace();
				}
				return vo;
			}).collect(Collectors.toList());
			searchResponseVo.setNavs(navsVoList);
		}
		if (paramVo.getBrandId() != null && paramVo.getBrandId().size() > 0) {
			List<SearchResponseVo.NavsVo> navs = searchResponseVo.getNavs();
			SearchResponseVo.NavsVo navsVo = new SearchResponseVo.NavsVo();
			navsVo.setNavName("品牌");
			R brandInfos = productFeignService.brandInfos(paramVo.getBrandId());
			StringBuffer brandInfoBuffer = new StringBuffer();
			if (brandInfos.getCode() == 0) {
				List<Map> brandMap = (List<Map>) brandInfos.get("brandInfo");
				String queryString = paramVo.getQueryString();
				for (Map map : brandMap) {
					brandInfoBuffer.append(map.get("name").toString()).append(";");
					try {
						String e = URLEncoder.encode(map.get("brandId").toString(), "utf-8");
						e = e.replace("+", "%20");
						queryString = queryString.replaceAll("&brandId=" + e, "")
								.replaceAll("\\?brandId=" + e, "").replaceAll("brandId=" + e, "");
					} catch (UnsupportedEncodingException unsupportedEncodingException) {
						unsupportedEncodingException.printStackTrace();
					}
				}
				navsVo.setLink("http://search.smilemall.cn/list.html?" + queryString);
				navsVo.setNavValue(brandInfoBuffer.toString());
				navs.add(navsVo);
			}
			searchResponseVo.setNavs(navs);
		}
		return searchResponseVo;
	}
	
	private SearchRequest buildSearchRequest(SearchParamVo searchParamVo) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if (!StringUtils.isEmpty(searchParamVo.getKeyword())) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			highlightBuilder.field("skuTitle").preTags("<b style='color: red'>").postTags("</b>");
			searchSourceBuilder.highlighter(highlightBuilder);
			boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParamVo.getKeyword()));
		}
		
		if (searchParamVo.getCatalog3Id() != null) {
			boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParamVo.getCatalog3Id()));
		}
		if (searchParamVo.getBrandId() != null && searchParamVo.getBrandId().size() != 0) {
			boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParamVo.getBrandId()));
		}
		if (searchParamVo.getHasStock() != null) {
			boolQueryBuilder.filter(QueryBuilders.termsQuery("hasStock", searchParamVo.getHasStock() == 1));
		}
		if (!StringUtils.isEmpty(searchParamVo.getSkuPrice())) {
			RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
			String[] price = searchParamVo.getSkuPrice().split("_");
			int maxLength = 2;
			if (price.length == maxLength) {
				rangeQuery.gte(price[0]).lte(price[1]);
			} else if (price.length == 1) {
				if (searchParamVo.getSkuPrice().startsWith(ATTR_SPLIT_SYMBOL)) {
					rangeQuery.lte(price[0]);
				} else if (searchParamVo.getSkuPrice().endsWith(ATTR_SPLIT_SYMBOL)) {
					rangeQuery.gte(price[0]);
				}
			}
			boolQueryBuilder.filter(rangeQuery);
		}
		
		if (searchParamVo.getAttrs() != null && searchParamVo.getAttrs().size() != 0) {
			for (String attr : searchParamVo.getAttrs()) {
				BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
				String[] attrArrays = attr.split("_");
				String attrId = attrArrays[0];
				String[] attrValues = attrArrays[1].split(":");
				nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrId", attrId))
						.must(QueryBuilders.termsQuery("attrs.attrValues", attrValues));
				NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
				
				boolQueryBuilder.filter(nestedQueryBuilder);
			}
		}
		searchSourceBuilder.query(boolQueryBuilder);
		
		if (!StringUtils.isEmpty(searchParamVo.getSort())) {
			String[] sort = searchParamVo.getSort().split(ATTR_SPLIT_SYMBOL);
			SortOrder sortOrder = "asc".equalsIgnoreCase(sort[1]) ? SortOrder.ASC : SortOrder.DESC;
			searchSourceBuilder.sort(sort[0], sortOrder);
		}
		
		int from = (searchParamVo.getPageNum() - 1) * EsConstant.PAGE_SIZE;
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(EsConstant.PAGE_SIZE);
		
		TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg").field("brandId").size(50);
		brandAgg.subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName").size(1));
		brandAgg.subAggregation(AggregationBuilders.terms("brandImgAgg").field("brandImg").size(1));
		searchSourceBuilder.aggregation(brandAgg);
		
		TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalogAgg").field("catalogId").size(20);
		catalogAgg.subAggregation(AggregationBuilders.terms("catalogName").field("catalogName").size(1));
		searchSourceBuilder.aggregation(catalogAgg);
		
		NestedAggregationBuilder nestedAttrAgg = AggregationBuilders.nested("attrAgg", "attrs");
		TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(50);
		attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));
		attrIdAgg.subAggregation(AggregationBuilders.terms("attrValuesAgg").field("attrs.attrValues").size(50));
		nestedAttrAgg.subAggregation(attrIdAgg);
		searchSourceBuilder.aggregation(nestedAttrAgg);
		
		return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
	}
}
