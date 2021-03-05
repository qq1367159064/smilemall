package cn.smile.smilemall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.smile.common.constant.ProductConstant;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.product.dao.AttrAttrgroupRelationDao;
import cn.smile.smilemall.product.dao.AttrDao;
import cn.smile.smilemall.product.dao.AttrGroupDao;
import cn.smile.smilemall.product.dao.CategoryDao;
import cn.smile.smilemall.product.entity.AttrAttrgroupRelationEntity;
import cn.smile.smilemall.product.entity.AttrEntity;
import cn.smile.smilemall.product.entity.AttrGroupEntity;
import cn.smile.smilemall.product.entity.CategoryEntity;
import cn.smile.smilemall.product.service.AttrService;
import cn.smile.smilemall.product.service.CategoryService;
import cn.smile.smilemall.product.vo.AttrRespVo;
import cn.smile.smilemall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
	
	@Autowired
	private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
	
	@Autowired
	private AttrGroupDao attrGroupDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private CategoryService categoryService;
	
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<AttrEntity> page = this.page(
				new Query<AttrEntity>().getPage(params),
				new QueryWrapper<>()
		);
		
		return new PageUtils(page);
	}
	
	
	@Override
	public boolean saveAttr(AttrVo attrVo) {
		AttrEntity attrEntity = new AttrEntity();
		BeanUtil.copyProperties(attrVo, attrEntity);
		// 保存基本数据
		this.save(attrEntity);
		// 保存关联关系
		if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
			AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
			attrAttrgroupRelationEntity.setAttrSort(0);
			attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
			attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
			attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
		}
		return true;
	}
	
	/**
	 * @param parameter 1
	 * @param cateId    2
	 * @return cn.smile.common.utils.PageUtils
	 * @Description 带条件查询对应分类的属性信息
	 * @author Smile
	 * @date 2021/1/23/023
	 */
	@Override
	public PageUtils queryBaseAttrPage(Map<String, Object> parameter, Long cateId, String type) {
		QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
		
		String key = parameter.get("key") != null ? parameter.get("key").toString() : null;
		queryWrapper.eq("attr_type", "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() :
				ProductConstant.AttrEnum.ATR_TYPE_SALE.getCode());
		if (cateId != 0) {
			queryWrapper.eq("catelog_id", cateId);
		}
		
		if (!StringUtils.isEmpty(key)) {
			queryWrapper.and(wrapper -> {
				wrapper.eq("attr_id", key).like("attr_name", key);
			});
		}
		IPage<AttrEntity> page = this.page(
				new Query<AttrEntity>().getPage(parameter),
				queryWrapper);
		List<AttrEntity> records = page.getRecords();
		List<AttrRespVo> respVos = records.stream().map(attrEntity -> {
			AttrRespVo attrRespVo = new AttrRespVo();
			BeanUtils.copyProperties(attrEntity, attrRespVo);
			// 如果是基本属性获取分组信息
			if ("base".equalsIgnoreCase(type)) {
				AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",
						attrEntity.getAttrId()));
				if (attrAttrgroupRelationEntity != null && attrAttrgroupRelationEntity.getAttrGroupId() != null) {
					AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
					if(attrGroupEntity != null) {
						attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
					}
				}
			}
			
			CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
			if (categoryEntity != null) {
				attrRespVo.setCatelogName(categoryEntity.getName());
			}
			return attrRespVo;
		}).collect(Collectors.toList());
		PageUtils pageUtils = new PageUtils(page);
		pageUtils.setList(respVos);
		return pageUtils;
		
	}
	
	/**
	 * @param attrVo 1
	 * @return boolean
	 * @Description 级联更新
	 * @author Smile
	 * @date 2021/1/23/023
	 */
	@Override
	@Transactional(rollbackFor = {Exception.class})
	public boolean updateAttr(AttrVo attrVo) {
		AttrEntity attrEntity = new AttrEntity();
		BeanUtils.copyProperties(attrVo, attrEntity);
		this.updateById(attrEntity);
		// 修改分组
		if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
			AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
			attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
			attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
			Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq(
					"attr_id", attrEntity.getAttrId()));
			if (count > 0) {
				attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>().eq(
						"attr_id", attrEntity.getAttrId()));
			} else {
				attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
			}
		}
		return true;
	}
	
	/**
	 * @param attrid 1
	 * @return cn.smile.smilemall.product.vo.AttrRespVo
	 * @Description 获取详细的属性分类信息
	 * @author Smile
	 * @date 2021/1/23/023
	 */
	@Override
	public AttrRespVo getAttrInfo(Long attrid) {
		AttrRespVo respVo = new AttrRespVo();
		AttrEntity attrEntity = this.getById(attrid);
		BeanUtils.copyProperties(attrEntity, respVo);
		// 获取分组
		if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
			AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
					attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrid));
			if (attrAttrgroupRelationEntity != null && attrAttrgroupRelationEntity.getAttrGroupId() != null) {
				respVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
				AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
				respVo.setGroupName(attrGroupEntity.getAttrGroupName());
			}
			Long[] categoryLogPath = categoryService.findCategoryLogPath(attrEntity.getCatelogId());
			respVo.setCatelogPath(categoryLogPath);
		}
		// 获取分类
		CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
		if (categoryEntity != null) {
			respVo.setCatelogName(categoryEntity.getName());
		}
		return respVo;
	}
	
	/**
	 * <p>获取所有用来检索的属性id</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param attrIds 1
	 * @return java.util.List<java.lang.Long>
	 */
	@Override
	public List<Long> getSearchTypeAttrIds(List<Long> attrIds) {
		try {
			return baseMapper.getSearchTypeAttrIds(attrIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}