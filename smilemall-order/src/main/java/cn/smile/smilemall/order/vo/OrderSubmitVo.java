package cn.smile.smilemall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>订单提交vo</p>
 *
 * @author smile
 * @date 2021-03-02
 */
@Data
public class OrderSubmitVo {
	
	private Long addrId;
	private Integer payType;
	private String orderToken;
	private BigDecimal payPrice;
	private String note;
}
