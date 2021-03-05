package cn.smile.smilemall.search.services;

import cn.smile.smilemall.search.vo.SearchParamVo;
import cn.smile.smilemall.search.vo.SearchResponseVo;

/**
 * <p></p>
 *
 * @author smile
 * @date  2021-02-18
 */
public interface MallSearchService {
	
	/**
	 * <p>检索接口</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @param searchParamVo 检索参数 1
	 * @return java.lang.Object
	 */
	SearchResponseVo search(SearchParamVo searchParamVo);
}
