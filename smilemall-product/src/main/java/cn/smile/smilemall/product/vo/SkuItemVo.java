package cn.smile.smilemall.product.vo;

import cn.smile.smilemall.product.entity.SkuImagesEntity;
import cn.smile.smilemall.product.entity.SkuInfoEntity;
import cn.smile.smilemall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>商品详情vo</p>
 *
 * @author smile
 * @date 2021-02-22
 */
@Data
public class SkuItemVo {
	private SkuInfoEntity skuInfoEntity;
	private List<SkuImagesEntity> skuImagesEntities;
	private SpuInfoDescEntity spuInfoDesc;
	private List<ItemSaleAttrVo> itemSaleAttrVos;
	private List<SpuItemBaseAttrVo> spuItemBaseAttrVos;
	private Boolean hasStock = true;
	@Data
	public static class ItemSaleAttrVo {
		private Long attrId;
		private String attrName;
		private List<AttrValueWithSkuId> attrValueWithSkuIds;
	}
	
	@Data
	public static class AttrValueWithSkuId {
		private String attrValue;
		private List<Long> skuId;
	}
	
	@Data
	public static class SpuItemBaseAttrVo {
		private String groupName;
		private List<SpuBaseAttrVo> spuBaseAttrVos = new ArrayList<>();
	}
	
	@Data
	public static class SpuBaseAttrVo {
		private String name;
		private String value;
	}
}
