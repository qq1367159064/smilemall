package cn.smile.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/24/024
 */
@Data
public class SkuReductionTo {
	private Long skuId;
	private int fullCount;
	private BigDecimal discount;
	private int countStatus;
	private BigDecimal fullPrice;
	private BigDecimal reducePrice;
	private int priceStatus;
	private List<MemberPrice> memberPrice;

}
