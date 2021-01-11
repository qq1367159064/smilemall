package cn.smile.smilemall.product.service.impl;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.product.dao.CategoryDao;
import cn.smile.smilemall.product.entity.CategoryEntity;
import cn.smile.smilemall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
	
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<CategoryEntity> page = this.page(
				new Query<CategoryEntity>().getPage(params),
				new QueryWrapper<CategoryEntity>()
		);
		
		return new PageUtils(page);
	}
	
	/**
	 * @Description 批量删除分类,没有引用的前提下
	 * @author Smile
	 * @date 2021/1/7
	 * @param menuIds 1
	 * @return java.lang.Integer
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
	 * @Description 递归获取所有子菜单
	 * @author Smile
	 * @date 2021/1/7
	 * @param currentMenu 1
 	 * @param allCategory 2
	 * @return java.util.List<cn.smile.smilemall.product.entity.CategoryEntity>
	 */
	private List<CategoryEntity> getChildrens(CategoryEntity currentMenu, List<CategoryEntity> allCategory) {
		return allCategory
		    .stream()
			.filter((categoryEntity -> categoryEntity.getParentCid().intValue() == currentMenu.getCatId().intValue()))
			.map(menu -> {
				menu.setChildren(getChildrens(menu, allCategory));
				return menu;
			})
			.sorted((menuStart, menuEnd) ->  (menuStart.getSort() == null ? 0 : menuStart.getSort()) - (menuEnd.getSort() == null ? 0 : menuEnd.getSort()))
			.collect(Collectors.toList());
	}
}