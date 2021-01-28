package cn.smile.smilemall.product.service.impl;

import cn.smile.smilemall.product.dao.BrandDao;
import cn.smile.smilemall.product.dao.CategoryDao;
import cn.smile.smilemall.product.entity.BrandEntity;
import cn.smile.smilemall.product.entity.CategoryEntity;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.product.dao.CategoryBrandRelationDao;
import cn.smile.smilemall.product.entity.CategoryBrandRelationEntity;
import cn.smile.smilemall.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
	
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private CategoryDao categoryDao;
	
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<CategoryBrandRelationEntity> page = this.page(
				new Query<CategoryBrandRelationEntity>().getPage(params),
				new QueryWrapper<CategoryBrandRelationEntity>()
		);
		
		return new PageUtils(page);
	}
	
	
	/**
	 * @param categoryBrandRelationEntity 1
	 * @return int
	 * @Description 保存包含有品牌名称和分类名称
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int saveDetail(CategoryBrandRelationEntity categoryBrandRelationEntity) {
		Long brandId = categoryBrandRelationEntity.getBrandId();
		Long catelogId = categoryBrandRelationEntity.getCatelogId();
		BrandEntity brandEntity = brandDao.selectById(brandId);
		CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
		categoryBrandRelationEntity.setBrandName(brandEntity.getName());
		categoryBrandRelationEntity.setCatelogName(categoryEntity.getName());
		return baseMapper.insert(categoryBrandRelationEntity);
	}
	
	/**
	 * @param brandId   1
	 * @param brandName 2
	 * @return int
	 * @Description 更新品牌的名称
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public boolean updateBrand(Long brandId, String brandName) {
		CategoryBrandRelationEntity brandEntity = new CategoryBrandRelationEntity();
		brandEntity.setBrandName(brandName);
		brandEntity.setBrandId(brandId);
		return this.update(brandEntity, new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
	}
	
	/**
	 * @param categoryId   1
	 * @param categoryName 2
	 * @return boolean
	 * @Description 更新分类信息
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public boolean updateCategory(Long categoryId, String categoryName) {
		CategoryBrandRelationEntity categoryEntity = new CategoryBrandRelationEntity();
		categoryEntity.setCatelogName(categoryName);
		categoryEntity.setCatelogId(categoryId);
		return this.update(categoryEntity, new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", categoryId));
	}
	
	/**
	 * @Description 获取分类{catId}下的所有品牌
	 * @author Smile
	 * @date 2021/1/24/024
	 * @param catId 1
	 * @return java.util.List<cn.smile.smilemall.product.entity.BrandEntity>
	 */
	@Override
	public List<BrandEntity> queryCategoryBrand(Long catId) {
		List<CategoryBrandRelationEntity> brandRelationEntities = baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
		List<Long> brandIds = brandRelationEntities.stream().map(item -> {
			return item.getBrandId();
		}).collect(Collectors.toList());
		if(brandIds == null || brandIds.size() == 0) {
			return null;
		}
		return brandDao.selectBatchIds(brandIds);
	}
}

