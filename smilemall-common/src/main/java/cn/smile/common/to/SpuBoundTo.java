package cn.smile.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/24/024
 */
@Data
public class SpuBoundTo {
	
	private Long spuId;
	private BigDecimal buyBounds;
	private BigDecimal growBounds;
}
