package cn.smile.smilemall.product.vo;

import lombok.Data;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/23/023
 */
@Data
public class AttrRespVo extends AttrVo{
	
	private String catelogName;
	private String groupName;
	
	private Long[] catelogPath;
}
