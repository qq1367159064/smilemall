package cn.smile.smilemall.product.service.impl;

import cn.smile.common.constant.ProductConstant;
import cn.smile.smilemall.product.dao.AttrAttrgroupRelationDao;
import cn.smile.smilemall.product.dao.AttrDao;
import cn.smile.smilemall.product.entity.AttrAttrgroupRelationEntity;
import cn.smile.smilemall.product.entity.AttrEntity;
import cn.smile.smilemall.product.service.AttrService;
import cn.smile.smilemall.product.vo.AttrGroupRelationVo;
import cn.smile.smilemall.product.vo.AttrGroupWithAttrsVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.product.dao.AttrGroupDao;
import cn.smile.smilemall.product.entity.AttrGroupEntity;
import cn.smile.smilemall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
	@Autowired
	private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
	@Autowired
	private AttrDao attrDao;
	
	@Autowired
	private AttrService attrService;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String key = params.get("key") == null ? null : params.get("key").toString();
		QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
		
		if (!StringUtils.isEmpty(key)) {
			queryWrapper.eq("atr_group_id", key).or().like("attr_group_name", key);
		}
		IPage<AttrGroupEntity> page = this.page(
				new Query<AttrGroupEntity>().getPage(params),
				queryWrapper
		);
		
		return new PageUtils(page);
	}
	
	/**
	 * @param params 1
	 * @param catId  2
	 * @return cn.smile.common.utils.PageUtils
	 * @Description 根据分类的id获取对应分分类所有的属性组
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Override
	public PageUtils queryPage(Map<String, Object> params, Long catId) {
		if (catId == 0 || catId == null) {
			return queryPage(params);
		}
		String key = params.get("key") == null ? null : params.get("key").toString();
		QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_Id", catId);
		if (!StringUtils.isEmpty(key)) {
			queryWrapper.and(obj -> {
				obj.eq("atr_group_id", key).or().like("attr_group_name", key);
			});
		}
		IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
				queryWrapper);
		return new PageUtils(page);
	}
	
	/**
	 * @param groupId 1
	 * @return java.util.List<cn.smile.smilemall.product.entity.AttrEntity>
	 * @Description 获取分组的所有属性值
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@Override
	public List<AttrEntity> queryAttrList(Long groupId) {
		List<AttrAttrgroupRelationEntity> attrGroup =
				attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", groupId));
		List<Long> attrIds = attrGroup.stream().map(item -> {
			return item.getAttrId();
		}).collect(Collectors.toList());
		if (attrIds == null || attrIds.size() == 0) {
			return null;
		}
		List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
		return attrEntities;
	}
	
	@Override
	public boolean deleteRelation(AttrGroupRelationVo[] vos) {
		List<AttrAttrgroupRelationEntity> collect = Arrays.stream(vos).map(item -> {
			AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
			BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
			return attrAttrgroupRelationEntity;
		}).collect(Collectors.toList());
		return attrAttrgroupRelationDao.deleteBatchRelation(collect);
	}
	
	/**
	 * @param params      1
	 * @param attrgroupId 2
	 * @return cn.smile.common.utils.PageUtils
	 * @Description 获取分组没有关联的属性
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@Override
	public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
		// 只能获取同一分类的属性
		// 只能获取同一分类下没有被引用的属性
		
		// 获取自身分类的信息
		AttrGroupEntity attrGroupEntity = baseMapper.selectById(attrgroupId);
		Long catelogId = attrGroupEntity.getCatelogId();
		// 获取同一分类下的所有分组
		List<AttrGroupEntity> attrGroupEntities =
				baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
		// 获取所有分组id
		List<Long> collect = attrGroupEntities.stream().map(item -> {
			return item.getAttrGroupId();
		}).collect(Collectors.toList());
		// 获取其他分组关联的属性
		QueryWrapper<AttrAttrgroupRelationEntity> relationEntityQueryWrapper =
				new QueryWrapper<AttrAttrgroupRelationEntity>();
		if (collect != null && collect.size() != 0) {
			relationEntityQueryWrapper.in("attr_group_id", collect);
		}
		List<AttrAttrgroupRelationEntity> attrGroupId =
				attrAttrgroupRelationDao.selectList(relationEntityQueryWrapper);
		// 获取其他分离柱关联属性的id
		List<Long> attrIds = attrGroupId.stream().map(item -> {
			return item.getAttrId();
		}).collect(Collectors.toList());
		// 获取本分类其他分组没有关联的属性
		QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",
				ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
		if (attrIds != null && attrIds.size() != 0) {
			queryWrapper.notIn("attr_id", attrIds);
		}
		String key = params.get("key") == null ? null : params.get("key").toString();
		if (!StringUtils.isEmpty(key)) {
			queryWrapper.and(wrapper -> {
				wrapper.eq("attr_id", key).like("attr_name", key);
			});
		}
		IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), queryWrapper);
		
		return new PageUtils(page);
	}
	
	/**
	 * @Description 根据分类获取所有的分组和分组中的所有属性
	 * @author Smile
	 * @date 2021/1/24/024
	 * @param catelongId 1
	 * @return java.util.List<cn.smile.smilemall.product.vo.AttrGroupWithAttrsVo>
	 */
	@Override
	public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCateLogId(Long catelongId) {
		// 获取所有分组
		List<AttrGroupEntity> attrGroup = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelongId));
		// 查询所有属性
		return attrGroup.stream().map(item -> {
			AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
			BeanUtils.copyProperties(item, attrGroupWithAttrsVo);
			List<AttrEntity> attrEntities = queryAttrList(attrGroupWithAttrsVo.getAttrGroupId());
			if(attrEntities == null || attrEntities.size() == 0) {
				attrEntities = new ArrayList<>();
			}
			attrGroupWithAttrsVo.setAttrs(attrEntities);
			return attrGroupWithAttrsVo;
		}).collect(Collectors.toList());
	}
}