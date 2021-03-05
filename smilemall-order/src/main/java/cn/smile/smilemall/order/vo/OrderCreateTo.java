package cn.smile.smilemall.order.vo;

import cn.smile.smilemall.order.entity.OrderEntity;
import cn.smile.smilemall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-02
 */
@Data
public class OrderCreateTo {
	
	private OrderEntity order;
	private List<OrderItemEntity> orderItemEntities;
	private BigDecimal payPrice;
	private BigDecimal fare;
	
}
