/**
  * Copyright 2019 bejson.com 
  */
package cn.smile.smilemall.product.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @author Smile
 * @date 2021/1/24/024
 * @return
 */
@Data
public class Skus {

    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Images> images;
    private List<String> descar;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;


}