package cn.smile.smilemall.product.service.impl;

import cn.smile.common.to.SkuReductionTo;
import cn.smile.common.to.SpuBoundTo;
import cn.smile.common.utils.R;
import cn.smile.smilemall.product.entity.*;
import cn.smile.smilemall.product.feignImpl.CouponFeignService;
import cn.smile.smilemall.product.service.*;
import cn.smile.smilemall.product.vo.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @Description 
 * @author Smile
 * @date 2021/1/24/024
 * @return
 */  
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
	@Autowired
	private SpuInfoDescService spuInfoDescService;
	@Autowired
	private AttrService attrService;
	@Autowired
	private SpuImagesService spuImagesService;
	@Autowired
	private ProductAttrValueService productAttrValueService;
	@Autowired
	private SkuImagesService skuImagesService;
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private SkuSaleAttrValueService attrValueService;
	@Autowired
	private CouponFeignService couponFeignService;
	
	
	/**
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 * @Description 通用分页查询
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SpuInfoEntity> page = this.page(
				new Query<SpuInfoEntity>().getPage(params),
				new QueryWrapper<SpuInfoEntity>()
		);
		return new PageUtils(page);
	}
	
	/**
	 * @param spuSaveVo 1
	 * @return void
	 * @Description 保存spu信息
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void saveInfo(SpuSaveVo spuSaveVo) {
		// 保存spu基本的信息
		SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
		BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
		spuInfoEntity.setCreateTime(new Date());
		spuInfoEntity.setUpdateTime(new Date());
		this.save(spuInfoEntity);
		// 保存spu的描述图片
		List<String> decript = spuSaveVo.getDecript();
		SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
		descEntity.setSpuId(spuInfoEntity.getId());
		descEntity.setDecript(String.join(",", decript));
		this.saveSpuInfoDesc(descEntity);
		// 保存spu的图片集
		List<String> images = spuSaveVo.getImages();
		spuImagesService.saveSpuImages(spuInfoEntity.getId(), images);
		// 保存spu的规格参数
		List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
		List<ProductAttrValueEntity> productAttrValueEntityList = baseAttrs.stream().map(item -> {
			ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
			productAttrValueEntity.setSpuId(spuInfoEntity.getId());
			AttrEntity attrEntity = attrService.getById(item.getAttrId());
			productAttrValueEntity.setAttrName(attrEntity.getAttrName());
			productAttrValueEntity.setAttrValue(item.getAttrValues());
			productAttrValueEntity.setQuickShow(item.getShowDesc());
			return productAttrValueEntity;
		}).collect(Collectors.toList());
		productAttrValueService.saveBatch(productAttrValueEntityList);
		// 保存积分信息
		SpuBoundTo spuBoundTo = new SpuBoundTo();
		Bounds bounds = spuSaveVo.getBounds();
		BeanUtils.copyProperties(bounds, spuBoundTo);
		spuBoundTo.setSpuId(spuInfoEntity.getId());
		
		R spuR = couponFeignService.save(spuBoundTo);
		if (spuR.getCode() != 0) {
			log.error("远程调用失败");
		}
		
		// 保存sku的信息
		List<Skus> skus = spuSaveVo.getSkus();
		if (skus != null && skus.size() != 0) {
			SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
			skus.forEach(item -> {
				String defaultImg = "";
				for (Images image : item.getImages()) {
					if (image.getDefaultImg() == 1) {
						defaultImg = image.getImgUrl();
						break;
					}
				}
				BeanUtils.copyProperties(item, skuInfoEntity);
				skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
				skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
				skuInfoEntity.setSaleCount(0L);
				skuInfoEntity.setSpuId(spuInfoEntity.getCatalogId());
				skuInfoEntity.setSkuDefaultImg(defaultImg);
				skuInfoService.save(skuInfoEntity);
				Long skuId = skuInfoEntity.getSkuId();
				// 处理图片信息
				List<SkuImagesEntity> skuImages = item.getImages().stream().map(img -> {
					SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
					skuImagesEntity.setSkuId(skuId);
					skuImagesEntity.setImgUrl(img.getImgUrl());
					skuImagesEntity.setDefaultImg(img.getDefaultImg());
					return skuImagesEntity;
				}).filter(itex-> !StringUtils.isEmpty(itex.getImgUrl())).collect(Collectors.toList());
				
				// 保存sku的所有图片
				skuImagesService.saveBatch(skuImages);
				// 保存销售属性
				List<Attr> attr = item.getAttr();
				List<SkuSaleAttrValueEntity> attrList = attr.stream().map(ar -> {
					SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
					BeanUtils.copyProperties(ar, attrValueEntity);
					attrValueEntity.setSkuId(skuId);
					return attrValueEntity;
				}).collect(Collectors.toList());
				attrValueService.saveBatch(attrList);
				// 保存优惠信息
				SkuReductionTo skuReductionTo = new SkuReductionTo();
				BeanUtils.copyProperties(item, skuReductionTo);
				skuReductionTo.setSkuId(skuId);
				if(skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1 || skuReductionTo.getFullCount() > 0) {
					R skuR = couponFeignService.saveSkuReduction(skuReductionTo);
					if (skuR.getCode() != 0) {
						log.error("sku优惠远程调用失败");
					}
				}
				
			});
		}
	}
	
	/**
	 * @param spuInfoEntity 1
	 * @return void
	 * @Description 保存spu信息
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@Override
	public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
		this.baseMapper.insert(spuInfoEntity);
	}
	
	/**
	 * @param descEntity 1
	 * @return void
	 * @Description 保存spu的描述信息
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@Override
	public void saveSpuInfoDesc(SpuInfoDescEntity descEntity) {
		
		spuInfoDescService.save(descEntity);
	}
	
	/**
	 * @Description 检索查询
	 * @author Smile
	 * @date 2021/1/25/025
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 */
	@Override
	public PageUtils queryPageByCondition(Map<String, Object> params) {
		QueryWrapper<SpuInfoEntity> spuInfoEntityQueryWrapper = new QueryWrapper<>();
		
		if (!StringUtils.isEmpty(params.get("key"))) {
			spuInfoEntityQueryWrapper.and(w -> {
				w.eq("id", params.get("key").toString()).or().like("spuName", params.get("key").toString());
			});
		}
		if(!StringUtils.isEmpty(params.get("status"))) {
			spuInfoEntityQueryWrapper.and(w -> {
				w.eq("publish_status", params.get("status").toString());
			});
		}
		if(!StringUtils.isEmpty(params.get("brandId"))) {
			spuInfoEntityQueryWrapper.eq("brand_id", params.get("brandId").toString());
		}
		if(!StringUtils.isEmpty(params.get("catelogId"))) {
			spuInfoEntityQueryWrapper.eq("catelog_id", params.get("catelogId").toString());
		}
		
		IPage<SpuInfoEntity> page = this.page(
				new Query<SpuInfoEntity>().getPage(params),
				spuInfoEntityQueryWrapper
		);
		return new PageUtils(page);
	}
}