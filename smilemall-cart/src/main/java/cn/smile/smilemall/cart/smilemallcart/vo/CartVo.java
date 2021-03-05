package cn.smile.smilemall.cart.smilemallcart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>购物车</p>
 *
 * @author smile
 * @date 2021-02-25
 */
public class CartVo {
	
	private List<CartItemVo> cartItemVos;
	private Integer countNum;
	private Integer countType;
	/**
	 * 总价格
	 */
	private BigDecimal totalAmount;
	/**
	 * 优惠
	 */
	private BigDecimal reduce = new BigDecimal("0.00");
	
	public List<CartItemVo> getCartItemVos() {
		return cartItemVos;
	}
	
	public void setCartItemVos(List<CartItemVo> cartItemVos) {
		this.cartItemVos = cartItemVos;
	}
	
	public Integer getCountNum() {
		int count = 0;
		if(this.cartItemVos != null && this.cartItemVos.size() != 0) {
			for (CartItemVo cartItemVo : cartItemVos) {
				count += cartItemVo.getCount();
			}
		}
		return countNum = count;
	}
	
	public Integer getCountType() {
		int count = 0;
		if(this.cartItemVos != null && this.cartItemVos.size() != 0) {
			for (CartItemVo cartItemVo : cartItemVos) {
				count ++;
			}
		}
		return countType = count;
	}
	
	public BigDecimal getTotalAmount() {
		BigDecimal amount = new BigDecimal("0");
		if(this.cartItemVos != null && this.cartItemVos.size() != 0) {
			for (CartItemVo cartItemVo : cartItemVos) {
				BigDecimal totalPrice = cartItemVo.getTotalPrice();
				amount = amount.add(totalPrice);
			}
		}
		return totalAmount = amount.subtract(getReduce());
	}
	
	public BigDecimal getReduce() {
		return reduce;
	}
	
	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
	}
}
