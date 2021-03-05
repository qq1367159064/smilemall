package cn.smile.smilemall.product.service.impl;

import cn.smile.common.annotation.cache.UseCache;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.product.dao.SkuInfoDao;
import cn.smile.smilemall.product.entity.SkuImagesEntity;
import cn.smile.smilemall.product.entity.SkuInfoEntity;
import cn.smile.smilemall.product.entity.SpuInfoDescEntity;
import cn.smile.smilemall.product.service.*;
import cn.smile.smilemall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
	@Autowired
	private SkuImagesService skuImagesService;
	@Autowired
	private SpuInfoDescService spuInfoDescService;
	@Autowired
	private AttrGroupService attrGroupService;
	@Autowired
	private SkuSaleAttrValueService skuSaleAttrValueService;
	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;
	
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SkuInfoEntity> page = this.page(
				new Query<SkuInfoEntity>().getPage(params),
				new QueryWrapper<SkuInfoEntity>()
		);
		
		return new PageUtils(page);
	}
	
	/**
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 * @Description 检索sku
	 * @author Smile
	 * @date 2021/1/27/027
	 */
	@Override
	public PageUtils queryPageCondition(Map<String, Object> params) {
		QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<>();
		
		String key = params.get("key") != null ? params.get("key").toString() : "";
		if (!StringUtils.isEmpty(key)) {
			skuInfoEntityQueryWrapper.and(w -> {
				w.eq("sku_id", key).or().like("sku_name", key);
			});
		}
		Integer catelogid = params.get("catelogId") != null ? Integer.parseInt(params.get("catelogId").toString()) : null;
		if (!StringUtils.isEmpty(catelogid) && catelogid != 0) {
			skuInfoEntityQueryWrapper.eq("catelog_id", catelogid);
		}
		
		Integer brandId = params.get("brandId") != null ? Integer.parseInt(params.get("brandId").toString()) : null;
		if (!StringUtils.isEmpty(brandId) && brandId != 0) {
			skuInfoEntityQueryWrapper.eq("brand_id", brandId);
		}
		
		BigDecimal min = params.get("min") != null ? new BigDecimal(Double.parseDouble(params.get("min").toString())) : null;
		if (!StringUtils.isEmpty(min) && min.compareTo(new BigDecimal(0)) == 1) {
			skuInfoEntityQueryWrapper.ge("price", min.intValue());
		}
		
		BigDecimal max = params.get("max") != null ? new BigDecimal(Double.parseDouble(params.get("max").toString())) : null;
		if (!StringUtils.isEmpty(max) && max.compareTo(new BigDecimal(0)) == 1) {
			skuInfoEntityQueryWrapper.eq("price", max.intValue());
		}
		
		IPage<SkuInfoEntity> page = this.page(
				new Query<SkuInfoEntity>().getPage(params),
				skuInfoEntityQueryWrapper
		);
		return new PageUtils(page);
	}
	
	/**
	 * @param spuId 1
	 * @return java.util.List<cn.smile.smilemall.product.entity.SkuInfoEntity>
	 * @description 根据spuId获取所有sku信息
	 * @author Smile
	 * @date 2021/2/14/014
	 */
	@Override
	public List<SkuInfoEntity> getSkuInfoBySpuId(Long spuId) {
		try {
			return this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * <p>查询商品详情</p>
	 *
	 * @param skuId 1
	 * @return cn.smile.smilemall.product.vo.SkuItemVo
	 * @author smile
	 * @date 2021/2/22/022
	 */
	@UseCache(cacheKeyPrefix = "skuItem")
	@Override
	public SkuItemVo selectSkuItem(Long skuId) {
		SkuItemVo skuItemVo = new SkuItemVo();
		CompletableFuture<SkuInfoEntity> skuInfo = CompletableFuture.supplyAsync(() -> {
			SkuInfoEntity info = getById(skuId);
			skuItemVo.setSkuInfoEntity(info);
			return info;
		}, threadPoolExecutor);
		
		CompletableFuture<Void> skuInfoFuture = skuInfo.thenAcceptAsync(res -> {
			SpuInfoDescEntity spuInfo = spuInfoDescService.getById(res.getSpuId());
			skuItemVo.setSpuInfoDesc(spuInfo);
		}, threadPoolExecutor);
		
		CompletableFuture<Void> spuItemBaseFuture = skuInfo.thenAcceptAsync(res -> {
			List<SkuItemVo.SpuItemBaseAttrVo> spuItemBaseAttrVos =
					attrGroupService.selectGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
			skuItemVo.setSpuItemBaseAttrVos(spuItemBaseAttrVos);
		}, threadPoolExecutor);
		
		CompletableFuture<Void> itemSaleFuture = skuInfo.thenAcceptAsync(res -> {
			List<SkuItemVo.ItemSaleAttrVo> itemSaleAttrVos = skuSaleAttrValueService.selectSaleBySpuId(res.getSpuId());
			skuItemVo.setItemSaleAttrVos(itemSaleAttrVos);
		}, threadPoolExecutor);
		
		CompletableFuture<Void> imgFuture = CompletableFuture.runAsync(() -> {
			List<SkuImagesEntity> skuImgSkuImgList = skuImagesService.selectBySkuImgById(skuId);
			skuItemVo.setSkuImagesEntities(skuImgSkuImgList);
		}, threadPoolExecutor);
		
		try {
			CompletableFuture.allOf(skuInfoFuture, spuItemBaseFuture, itemSaleFuture, imgFuture).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return skuItemVo;
	}
	
	@Override
	public BigDecimal getSkuPrice(Long skuId) {
		SkuInfoEntity skuInfoEntity =
				baseMapper.selectOne(new LambdaQueryWrapper<SkuInfoEntity>().eq(SkuInfoEntity::getSkuId, skuId));
		return skuInfoEntity.getPrice();
	}
}
