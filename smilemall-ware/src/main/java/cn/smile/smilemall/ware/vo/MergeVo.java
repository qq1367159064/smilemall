package cn.smile.smilemall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/28/028
 */
@Data
public class MergeVo {
	
	private Long purchaseId;
	
	private List<Long> items;
}
