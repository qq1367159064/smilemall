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
public class SpuSaveVo {

    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;



}