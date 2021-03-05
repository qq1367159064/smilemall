package cn.smile.smilemall.product.service.impl;

import cn.hutool.core.lang.ObjectId;
import cn.smile.common.annotation.cache.UseCache;
import cn.smile.common.constant.RedisConstant;
import cn.smile.common.smilemall.utils.SmileMallRedisTemplate;
import cn.smile.common.smilemall.utils.SmileRedissonTemplate;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.product.dao.CategoryDao;
import cn.smile.smilemall.product.entity.CategoryEntity;
import cn.smile.smilemall.product.service.CategoryBrandRelationService;
import cn.smile.smilemall.product.service.CategoryService;
import cn.smile.smilemall.product.vo.CategoryLeaveTwoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author smile
 */
@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
	
	@Autowired
	private CategoryBrandRelationService categoryBrandRelationService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private RedissonClient redissonClient;
	
	private static Map<String, Map<String, List<CategoryLeaveTwoVo>>> cacheCategory = new LinkedHashMap<>();
	
	
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
	 * @param categoryEntity 1
	 * @return boolean
	 * @Description 级联更新关联数据
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Transactional(rollbackFor = {Exception.class})
	@Override
	@UseCache(cacheKeyPrefix = "category", useLock = true, operationType = "update")
	public boolean updateDetail(CategoryEntity categoryEntity) {
		this.updateById(categoryEntity);
		if (!StringUtils.isEmpty(categoryEntity.getName())) {
			categoryBrandRelationService.updateCategory(categoryEntity.getCatId(), categoryEntity.getName());
		}
		return true;
	}
	
	/**
	 * <p>返回所有一级分类</p>
	 *
	 * @return java.util.List<cn.smile.smilemall.product.entity.CategoryEntity>
	 * @author Smile
	 * @date 2021/2/15/015
	 */
	@Override
	@UseCache(cacheKeyPrefix = "category", useLock = true, operationType = "select")
	public List<CategoryEntity> getLeveOneCategory() {
		return list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
	}
	
	
	/**
	 * <p>获取分类数据</p>
	 *
	 * @return java.util.Map<java.lang.String, java.util.List < cn.smile.smilemall.product.vo.CategoryLeaveTwoVo>>
	 * @author Smile
	 * @date 2021/2/16/016
	 */
	@Override
	@UseCache(cacheKeyPrefix = "categoryJson", useLock = true, lockType = RLock.class)
	public Map<String, List<CategoryLeaveTwoVo>> getCatalogJson() {
		return selectFromDataAopSource();
	}
	
	/**
	 * <p>获取分类编程式缓存数据</p>
	 *
	 * @return java.util.Map<java.lang.String, java.util.List < cn.smile.smilemall.product.vo.CategoryLeaveTwoVo>>
	 * @author Smile
	 * @date 2021/2/16/016
	 */
	public Map<String, List<CategoryLeaveTwoVo>> getCatalogProgrammaticJson() {
		Map<String, List<CategoryLeaveTwoVo>> catalogDataCache =
				(Map<String, List<CategoryLeaveTwoVo>>) SmileMallRedisTemplate.getValue("catalogDataCache");
		if (catalogDataCache != null) {
			return catalogDataCache;
		}
		Map<String, List<CategoryLeaveTwoVo>> catalogJsonFromDb = getCatalogJsonRedissonLock();
		return catalogJsonFromDb;
	}
	
	/**
	 * <p>Redisson分布式锁获取</p>
	 *
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @author Smile
	 * @date 2021/2/15/015
	 */
	public Map<String, List<CategoryLeaveTwoVo>> getCatalogJsonRedissonLock() {
		SmileRedissonTemplate.lock("categoryJson-lock");
		Map<String, List<CategoryLeaveTwoVo>> returnCategoryData;
		try {
			returnCategoryData = selectFromDataSource();
		} finally {
			SmileRedissonTemplate.unLock("categoryJson-lock");
		}
		return returnCategoryData;
	}
	
	/**
	 * <p>分布式锁获取</p>
	 *
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @author Smile
	 * @date 2021/2/15/015
	 */
	public Map<String, List<CategoryLeaveTwoVo>> getCatalogJsonRedisLock() {
		String uuid = ObjectId.next();
		// 获取分布式锁
		Boolean lock = SmileMallRedisTemplate.setIfAbsent("lock", uuid, RedisConstant.Lock.TIME_OUT.getTimeOut());
		if (lock) {
			log.info("线程{}, 获取到了锁", Thread.currentThread().getName(), lock);
			long l = System.currentTimeMillis();
			Map<String, List<CategoryLeaveTwoVo>> returnCategoryData;
			try {
				returnCategoryData = selectFromDataSource();
			} finally {
				SmileMallRedisTemplate.execute(null, "lock", uuid);
			}
			log.info("耗费的时间{}", System.currentTimeMillis() - l);
			return returnCategoryData;
		} else {
			// 使用自锁的方式重试
			try {
				Thread.sleep(RedisConstant.Lock.SLEEP_TIME.getTimeOut());
			} catch (InterruptedException e) {
			}
			return getCatalogJsonRedisLock();
		}
	}
	
	/**
	 * <p>查询分类数据本地加锁</p>
	 *
	 * @return java.util.Map<java.lang.String, java.util.List < cn.smile.smilemall.product.vo.CategoryLeaveTwoVo>>
	 * @author Smile
	 * @date 2021/2/16/016
	 */
	public Map<String, List<CategoryLeaveTwoVo>> getCatalogJsonByLocalLock() {
		synchronized (this) {
			Map<String, List<CategoryLeaveTwoVo>> catalogListMap = selectFromDataSource();
			SmileMallRedisTemplate.setValue("catalogDataCache", catalogListMap, 180000L);
			return catalogListMap;
		}
	}
	
	/**
	 * <p>查询Aop缓存数据库</p>
	 *
	 * @return java.util.Map<java.lang.String, java.util.List < cn.smile.smilemall.product.vo.CategoryLeaveTwoVo>>
	 * @author Smile
	 * @date 2021/2/16/016
	 */
	public Map<String, List<CategoryLeaveTwoVo>> selectFromDataAopSource() {
		return getCommonCategory();
	}
	
	/**
	 * <p>通用查询分类方法</p>
	 * @author Smile
	 * @date 2021/2/18/018
	 * @return java.util.Map<java.lang.String,java.util.List<cn.smile.smilemall.product.vo.CategoryLeaveTwoVo>>
	 */
	public Map<String, List<CategoryLeaveTwoVo>> getCommonCategory() {
		List<CategoryEntity> allCategory = this.baseMapper.selectList(null);
		List<CategoryEntity> leveOneCategory = getParent_cid(allCategory, 0L);
		Map<String, List<CategoryLeaveTwoVo>> returnCatalogData = leveOneCategory.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
			// 获取二级分类数据
			List<CategoryEntity> childCategoryData = getParent_cid(allCategory, v.getCatId());
			List<CategoryLeaveTwoVo> catalogTwoData = null;
			if (childCategoryData != null && childCategoryData.size() != 0) {
				catalogTwoData = childCategoryData.stream().map(l2 -> {
					// 获取三级分类
					List<CategoryEntity> catalogThreeData = getParent_cid(allCategory, l2.getCatId());
					List<CategoryLeaveTwoVo.CatalogThreeVo> returnCatalogThreeData = null;
					if (catalogThreeData != null && catalogThreeData.size() != 0) {
						returnCatalogThreeData = catalogThreeData.stream().map(l3 -> {
							CategoryLeaveTwoVo.CatalogThreeVo catalogThreeVo = new CategoryLeaveTwoVo.CatalogThreeVo(l2.getCatId().toString(), l3.getCatId().toString(),
									l3.getName());
							return catalogThreeVo;
						}).collect(Collectors.toList());
					}
					CategoryLeaveTwoVo categoryLeaveTwoVo = new CategoryLeaveTwoVo(v.getCatId().toString(), returnCatalogThreeData,
							l2.getCatId().toString(), l2.getName());
					return categoryLeaveTwoVo;
				}).collect(Collectors.toList());
			}
			if (catalogTwoData == null) {
				catalogTwoData = new ArrayList<>();
			}
			return catalogTwoData;
		}));
		return returnCatalogData;
	}
	
	/**
	 * <p>查询编程式缓存数据库</p>
	 *
	 * @return java.util.Map<java.lang.String, java.util.List < cn.smile.smilemall.product.vo.CategoryLeaveTwoVo>>
	 * @author Smile
	 * @date 2021/2/16/016
	 */
	public Map<String, List<CategoryLeaveTwoVo>> selectFromDataSource() {
		Object catalogDataCache = SmileMallRedisTemplate.getValue("catalogDataCache");
		if (catalogDataCache != null) {
			return (Map<String, List<CategoryLeaveTwoVo>>) catalogDataCache;
		}
		log.info("查询了数据库");
		Map<String, List<CategoryLeaveTwoVo>> commonCategory = getCommonCategory();
		SmileMallRedisTemplate.setValue("catalogDataCache", commonCategory, RedisConstant.Lock.UNIT_TIME.getTimeOut());
		return commonCategory;
	}
	
	/**
	 * <p>分类数据筛选</p>
	 *
	 * @param allCategory 1
	 * @param parent_cid  2
	 * @return java.util.List<cn.smile.smilemall.product.entity.CategoryEntity>
	 * @author Smile
	 * @date 2021/2/17/017
	 */
	private List<CategoryEntity> getParent_cid(List<CategoryEntity> allCategory, Long parent_cid) {
		return allCategory.stream().filter(item -> item.getParentCid().longValue() == parent_cid.longValue()).collect(Collectors.toList());
	}
}