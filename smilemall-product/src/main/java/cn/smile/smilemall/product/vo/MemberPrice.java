/**
  * Copyright 2019 bejson.com 
  */
package cn.smile.smilemall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @author Smile
 * @date 2021/1/24/024
 * @return
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}