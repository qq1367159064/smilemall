package cn.smile.smilemall.ware.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>购物项</p>
 *
 * @author smile
 * @date 2021-02-25
 */
public class OrderItemVo {
	
	/**
	 * skuid
	 */
	private Long skuId;
	
	/**
	 * 商品标题
	 */
	private String title;
	
	/**
	 * 商品图片
	 */
	private String image;
	
	/**
	 * sku属性
	 */
	private List<String> skuAttr;
	
	/**
	 * sku价格
	 */
	private BigDecimal price;
	
	/**
	 * 商品数量
	 */
	private Integer count;
	
	/**
	 * 商品的总价格
	 */
	private BigDecimal totalPrice;
	
	public Long getSkuId() {
		return skuId;
	}
	
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public List<String> getSkuAttr() {
		return skuAttr;
	}
	
	public void setSkuAttr(List<String> skuAttr) {
		this.skuAttr = skuAttr;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	/**
	 * <p>计算总价格</p>
	 * @author smile
	 * @date 2021/2/25/025
	 * @return java.math.BigDecimal
	 */
	public BigDecimal getTotalPrice() {
		return this.getPrice().multiply(new BigDecimal("" + this.count));
	}
	
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
}
