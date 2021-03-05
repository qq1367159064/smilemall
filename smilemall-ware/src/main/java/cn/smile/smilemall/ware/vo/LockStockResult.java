package cn.smile.smilemall.ware.vo;

import lombok.Data;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-03
 */
@Data
public class LockStockResult {
	
	private Long skuId;
	private Integer num;
	private Boolean locked;
}
