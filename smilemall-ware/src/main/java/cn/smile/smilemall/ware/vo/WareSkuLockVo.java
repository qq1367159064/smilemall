package cn.smile.smilemall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-03
 */
@Data
public class WareSkuLockVo {
	
	private Long skuId;
	private List<OrderItemVo> orderItemVos;
}
