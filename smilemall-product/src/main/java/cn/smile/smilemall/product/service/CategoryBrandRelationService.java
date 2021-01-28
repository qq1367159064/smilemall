package cn.smile.smilemall.product.service;

import cn.smile.smilemall.product.entity.BrandEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    int saveDetail(CategoryBrandRelationEntity categoryBrandRelationEntity);

    
    boolean updateBrand(Long brandId, String brandName);
    
    boolean updateCategory(Long categoryId, String categoryName);
	
	List<BrandEntity> queryCategoryBrand(Long catId);
}

