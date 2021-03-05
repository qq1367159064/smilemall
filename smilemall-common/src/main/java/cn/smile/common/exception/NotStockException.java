package cn.smile.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-03-03
 */
public class NotStockException extends RuntimeException{
	
	@Getter @Setter
	private Long skuId;
	public NotStockException(Long skuId) {
		super("库存不足" + skuId);
		this.skuId = skuId;
	}
}
