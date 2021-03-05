package cn.smile.smilemall.product.service.impl;

import cn.smile.common.constant.ProductConstant;
import cn.smile.common.to.SkuReductionTo;
import cn.smile.common.to.SpuBoundTo;
import cn.smile.common.to.es.SkuEsModule;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.common.utils.R;
import cn.smile.smilemall.product.dao.SpuInfoDao;
import cn.smile.smilemall.product.entity.*;
import cn.smile.smilemall.product.feignImpl.CouponFeignService;
import cn.smile.smilemall.product.feignImpl.EsFeignService;
import cn.smile.smilemall.product.feignImpl.WareFeignService;
import cn.smile.smilemall.product.service.*;
import cn.smile.smilemall.product.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
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
	@Autowired
	private WareFeignService wareFeignService;
	@Autowired
	private EsFeignService esFeignService;
	
	
	
	
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
		// 获取前端页面传过来的属性值
		List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
		List<ProductAttrValueEntity> productAttrValueEntityList = baseAttrs.stream().map(item -> {
			ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
			// 保存spuId
			productAttrValueEntity.setSpuId(spuInfoEntity.getId());
			// 根据属性id查询所有属性
			AttrEntity attrEntity = attrService.getById(item.getAttrId());
			productAttrValueEntity.setAttrId(attrEntity.getAttrId());
			productAttrValueEntity.setAttrName(attrEntity.getAttrName());
			productAttrValueEntity.setAttrValue(item.getAttrValues());
			productAttrValueEntity.setQuickShow(item.getShowDesc());
			productAttrValueEntity.setAttrSort(0);
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
				skuInfoEntity.setSpuId(spuInfoEntity.getId());
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
		if(!StringUtils.isEmpty(params.get("brandId")) && Integer.valueOf(params.get("brandId").toString()) != 0) {
			spuInfoEntityQueryWrapper.eq("brand_id", params.get("brandId").toString());
		}
		if(!StringUtils.isEmpty(params.get("catelogId")) && Integer.valueOf(params.get("catelogId").toString()) != 0) {
			spuInfoEntityQueryWrapper.eq("catelog_id", params.get("catelogId").toString());
		}
		
		IPage<SpuInfoEntity> page = this.page(
				new Query<SpuInfoEntity>().getPage(params),
				spuInfoEntityQueryWrapper
		);
		return new PageUtils(page);
	}
	
	/**
	 * <p>商品上架</p>
	 * @author Smile
	 * @date 2021/2/14/014
	 * @param spuId 1
	 * @return boolean
	 */
	@Override
	public boolean up(Long spuId) {
		// 获取对应spuId的所有sku
		List<SkuInfoEntity> skuInfoBySpuId = skuInfoService.getSkuInfoBySpuId(spuId);
		List<Long> skuIds = skuInfoBySpuId.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
		
		// 远程调用库存
		Map<Long, Map> _skuHasStocks = null;
		try {
			R r = wareFeignService.getSkuHasStock(skuIds);
			List<Map> skuHasStocks = (List<Map>) r.get("skuHasStock");
			_skuHasStocks = skuHasStocks.stream().collect(Collectors.toMap(ita -> Long.valueOf(ita.get("skuId").toString()),
					ita -> ita));
		} catch (Exception e) {
			log.error("库存服务调用异常{}", e);
			e.printStackTrace();
		}
		Map<Long, Map> final_skuHasStocks = _skuHasStocks;
		List<SkuEsModule> skuEsModules = skuInfoBySpuId.stream().map(item -> {
			SkuEsModule skuEsModule = new SkuEsModule();
			skuEsModule.setSkuPrice(item.getPrice());
			skuEsModule.setSkuImg(item.getSkuDefaultImg());
			// 保存属性信息
			List<ProductAttrValueEntity> spuAttrs = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq(
					"spu_id", item.getSpuId()));
			List<Long> attrIds = spuAttrs.stream().map(itx -> {
				return itx.getAttrId();
			}).collect(Collectors.toList());
			// 获取可以检索的属性id
			List<Long> searchTypeAttrIds = attrService.getSearchTypeAttrIds(attrIds);
			//Set<Long> _searchTypeAttrIds = new HashSet<>(searchTypeAttrIds);
			
			// 获取保存的数据
			List<SkuEsModule.Attrs> esAttrs = spuAttrs.stream().filter(ity -> {
				return searchTypeAttrIds.contains(ity.getAttrId());
			}).map(itz -> {
				SkuEsModule.Attrs attrs = new SkuEsModule.Attrs();
				attrs.setAttrValues(itz.getAttrValue());
				BeanUtils.copyProperties(itz, attrs);
				return attrs;
			}).collect(Collectors.toList());
			skuEsModule.setAttrs(esAttrs);
			// 保存品牌信息
			BrandEntity brandInfo = brandService.getById(item.getBrandId());
			skuEsModule.setBrandImg(brandInfo.getLogo());
			skuEsModule.setBrandName(brandInfo.getName());
			// 保存分类信息
			CategoryEntity categoryInfo = categoryService.getById(item.getCatalogId());
			skuEsModule.setCatalogName(categoryInfo.getName());
			skuEsModule.setHasStock(true);
			// 保存库存
			if(final_skuHasStocks != null) {
				Map skuHasStockVo = final_skuHasStocks.get(item.getSkuId());
				skuEsModule.setHasStock(Boolean.valueOf(skuHasStockVo.get("hasStock").toString()));
			}
			// TODO保存商品热度
			skuEsModule.setHotScore(0L);
			BeanUtils.copyProperties(item, skuEsModule);
			return skuEsModule;
		}).collect(Collectors.toList());
		
		R r = esFeignService.productStatusUp(skuEsModules);
		if(r.getCode() == 0) {
			SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
			spuInfoEntity.setId(spuId);
			spuInfoEntity.setUpdateTime(new Date());
			spuInfoEntity.setPublishStatus(ProductConstant.StatusEnum.SPU_UP.getCode());
			this.update(spuInfoEntity, new UpdateWrapper<SpuInfoEntity>().eq("id", spuId));
			return true;
		}
		return false;
	}
	
	@Override
	public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);
		return getById(skuInfo.getSpuId());
	}
}