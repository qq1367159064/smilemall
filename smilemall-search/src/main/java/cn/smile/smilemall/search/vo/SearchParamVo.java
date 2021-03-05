package cn.smile.smilemall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-18
 */
@Data
public class SearchParamVo {
	
	private String keyword;
	private Long catalog3Id;
	
	// 排序条件
	/**
	 * <p>
	 * 	排序条件
	 * 	sort = saleCount_asc/desc
	 * 	sort = price_asc/desc
	 * 	sort = hotScore_asc/desc
	 * </p>
	 * @date 2021/2/18/018
	 */
	private String sort;
	
	// 过滤条件
	/**
	 * <p>是否有货</p>
	 * @date 2021/2/18/018
	 */
	private Integer hasStock;
	
	/**
	 * <p>
	 * 	价格区间
	 * 	_500, 0_500, 500_
	 * </p>
	 * @date 2021/2/18/018
	 */
	private String skuPrice;
	
	private List<Long> brandId;
	
	private List<String> attrs;
	private Integer pageNum = 1;
	private String queryString;
}
