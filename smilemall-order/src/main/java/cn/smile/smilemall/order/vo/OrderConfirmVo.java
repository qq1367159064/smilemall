package cn.smile.smilemall.order.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-28
 */
public class OrderConfirmVo {
	
	/**
	 * 用户地址
	 */
	private List<MemberAddressVo> memberAddressVos;
	
	/**
	 * 购物项
	 */
	private List<OrderItemVo> orderItemVos;
	
	/**
	 * 用户积分
	 */
	private Integer integration;
	
	/**
	 * 订单总价格
	 */
	private BigDecimal totalPrice;
	
	/**
	 * 订单实际付款价格
	 */
	private BigDecimal payPrice;
	
	/**
	 * 订单中所有的商品数量
	 */
	private Integer count;
	
	@Getter @Setter
	private String orderToken;
	/**
	 * 判断是否有库存
	 */
	private Map<Long, Object> stocks;
	
	public Map<Long, Object> getStocks() {
		return stocks;
	}
	
	public void setStocks(Map<Long, Object> stocks) {
		this.stocks = stocks;
	}
	
	public Integer getCount() {
		this.count = 0;
		if (this.orderItemVos != null && this.orderItemVos.size() != 0) {
			for (OrderItemVo orderItemVo : orderItemVos) {
				this.count += orderItemVo.getCount();
			}
		}
		return count;
	}
	
	public List<MemberAddressVo> getMemberAddressVos() {
		return memberAddressVos;
	}
	
	public void setMemberAddressVos(List<MemberAddressVo> memberAddressVos) {
		this.memberAddressVos = memberAddressVos;
	}
	
	public List<OrderItemVo> getOrderItemVos() {
		return orderItemVos;
	}
	
	public void setOrderItemVos(List<OrderItemVo> orderItemVos) {
		this.orderItemVos = orderItemVos;
	}
	
	public Integer getIntegration() {
		return integration;
	}
	
	public void setIntegration(Integer integration) {
		this.integration = integration;
	}
	
	public BigDecimal getTotalPrice() {
		BigDecimal tempTotalPrice = new BigDecimal("0");
		if(orderItemVos != null) {
			for (OrderItemVo orderItemVo : orderItemVos) {
				tempTotalPrice =
						tempTotalPrice.add(orderItemVo.getPrice().multiply(new BigDecimal(orderItemVo.getCount().toString())));
			}
		}
		return totalPrice = tempTotalPrice;
	}
	
	public BigDecimal getPayPrice() {
		return payPrice = getTotalPrice();
	}

}
