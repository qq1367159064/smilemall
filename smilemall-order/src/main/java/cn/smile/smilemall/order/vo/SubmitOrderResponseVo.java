package cn.smile.smilemall.order.vo;

import cn.smile.smilemall.order.entity.OrderEntity;
import lombok.Data;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-02
 */
@Data
public class SubmitOrderResponseVo {

	private OrderEntity order;
	private Integer code;
	
}
