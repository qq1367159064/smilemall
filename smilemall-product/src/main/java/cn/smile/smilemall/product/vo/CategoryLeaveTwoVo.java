package cn.smile.smilemall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p> 二级分类vo</p>
 *
 * @author smile
 * @date 2021-02-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryLeaveTwoVo {
	
	private String catalog1Id;
	private List<CatalogThreeVo> catalog3List;
	private String id;
	private String name;
	
	/**
	 * <p>三级分类vo</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @return
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CatalogThreeVo {
		private String catalog2Id;
		private String id;
		private String name;
	}
}
