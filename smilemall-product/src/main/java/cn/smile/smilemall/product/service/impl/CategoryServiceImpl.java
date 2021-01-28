package cn.smile.smilemall.product.service.impl;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.product.dao.AttrAttrgroupRelationDao;
import cn.smile.smilemall.product.dao.AttrDao;
import cn.smile.smilemall.product.dao.CategoryDao;
import cn.smile.smilemall.product.entity.AttrAttrgroupRelationEntity;
import cn.smile.smilemall.product.entity.AttrEntity;
import cn.smile.smilemall.product.entity.CategoryEntity;
import cn.smile.smilemall.product.service.CategoryBrandRelationService;
import cn.smile.smilemall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
	
	@Autowired
	private CategoryBrandRelationService categoryBrandRelationService;

	
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<CategoryEntity> page = this.page(
				new Query<CategoryEntity>().getPage(params),
				new QueryWrapper<CategoryEntity>()
		);
		
		return new PageUtils(page);
	}
	
	/**
	 * @param menuIds 1
	 * @return java.lang.Integer
	 * @Description 批量删除分类, 没有引用的前提下
	 * @author Smile
	 * @date 2021/1/7
	 */
	@Override
	public Integer removeMenuByIds(List<Long> menuIds) {
		//TODO 检查当前删除菜单是否被其他地方引用
		// 逻辑删除
		return baseMapper.deleteBatchIds(menuIds);
	}
	
	/**
	 * @param
	 * @return java.util.List<cn.smile.smilemall.product.entity.CategoryEntity>
	 * @Description 查询所有分类并且以树形的格式返回
	 * @author Smile
	 * @date 2021/1/7
	 */
	@Override
	public List<CategoryEntity> listWithTree() {
		// 获取所有分类数组
		List<CategoryEntity> allCategoryEntities = baseMapper.selectList(null);
		// 获取顶级分类
		List<CategoryEntity> threeMenu = allCategoryEntities
				.stream()
				.filter(categoryEntity -> categoryEntity.getParentCid() == 0)
				.map(menu -> {
					menu.setChildren(getChildrens(menu, allCategoryEntities));
					return menu;
				})
				.sorted((menuStart, menuEnd) -> (menuStart.getSort() == null ? 0 : menuStart.getSort()) - (menuEnd.getSort() == null ? 0 : menuEnd.getSort()))
				.collect(Collectors.toList());
		return threeMenu;
	}
	
	/**
	 * @param currentMenu 1
	 * @param allCategory 2
	 * @return java.util.List<cn.smile.smilemall.product.entity.CategoryEntity>
	 * @Description 递归获取所有子菜单
	 * @author Smile
	 * @date 2021/1/7
	 */
	private List<CategoryEntity> getChildrens(CategoryEntity currentMenu, List<CategoryEntity> allCategory) {
		return allCategory
				.stream()
				.filter((categoryEntity -> categoryEntity.getParentCid().intValue() == currentMenu.getCatId().intValue()))
				.map(menu -> {
					menu.setChildren(getChildrens(menu, allCategory));
					return menu;
				})
				.sorted((menuStart, menuEnd) -> (menuStart.getSort() == null ? 0 : menuStart.getSort()) - (menuEnd.getSort() == null ? 0 : menuEnd.getSort()))
				.collect(Collectors.toList());
	}
	
	/**
	 * @param cateLogId 1
	 * @return java.lang.Long[]
	 * @Description 递归获取单一分类的所有层级
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Override
	public Long[] findCategoryLogPath(Long cateLogId) {
		List<Long> paths = new ArrayList<>();
		List<Long> parentPath = findParentPath(cateLogId, paths);
		Collections.reverse(parentPath);
		return parentPath.toArray(new Long[parentPath.size()]);
	}
	
	public List<Long> findParentPath(Long categoryId, List<Long> paths) {
		paths.add(categoryId);
		CategoryEntity currentCategory = getById(categoryId);
		if (currentCategory != null && currentCategory.getParentCid() != 0) {
			findParentPath(currentCategory.getParentCid(), paths);
		}
		return paths;
	}
	
	/**
	 * @Description 级联更新关联数据
	 * @author Smile
	 * @date 2021/1/15/015
	 * @param categoryEntity 1
	 * @return boolean
	 */
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public boolean updateDetail(CategoryEntity categoryEntity) {
		this.updateById(categoryEntity);
		if(!StringUtils.isEmpty(categoryEntity.getName())) {
			categoryBrandRelationService.updateCategory(categoryEntity.getCatId(), categoryEntity.getName());
		}
		return true;
	}
	

}