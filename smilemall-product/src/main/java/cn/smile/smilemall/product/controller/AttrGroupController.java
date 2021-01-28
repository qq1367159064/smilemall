package cn.smile.smilemall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import cn.smile.smilemall.product.entity.AttrEntity;
import cn.smile.smilemall.product.service.AttrAttrgroupRelationService;
import cn.smile.smilemall.product.service.CategoryService;
import cn.smile.smilemall.product.vo.AttrGroupRelationVo;
import cn.smile.smilemall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.smile.smilemall.product.entity.AttrGroupEntity;
import cn.smile.smilemall.product.service.AttrGroupService;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;


/**
 * 属性分组
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
	@Autowired
	private AttrGroupService attrGroupService;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private AttrAttrgroupRelationService attrAttrgroupRelationService;
	
	/**
	 * @Description 获取分类上所有的分组和分组属性
	 * @author Smile
	 * @date 2021/1/24/024
	 * @param catelongId 1
	 * @return cn.smile.common.utils.R
	 */
	@GetMapping("/{catelogId}/withattr")
	public R getAttrGroupWithattr(@PathVariable(value = "catelogId") Long catelongId) {
		// 获取当前分类的说有属性分组
		// 获取分组中所有的属性
		List<AttrGroupWithAttrsVo> attrGroupWithAttrsByCateLogId = attrGroupService.getAttrGroupWithAttrsByCateLogId(catelongId);
		return R.ok().put("data", attrGroupWithAttrsByCateLogId);
	}
	
	
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = attrGroupService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	/**
	 * @param groupId 1
	 * @return cn.smile.common.utils.R
	 * @Description 获取分组所有关联的属性
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@GetMapping("/{groupId}/attr/relation")
	public R getGroupAttr(@PathVariable(value = "groupId") Long groupId) {
		List<AttrEntity> attrEntities = attrGroupService.queryAttrList(groupId);
		return R.ok().put("data", attrEntities);
	}
	
	/**
	 * @param attrGroupId 1
	 * @param param       2
	 * @return cn.smile.common.utils.R
	 * @Description 获取分组没有关联的属性
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@GetMapping("/{attrGroupId}/noattr/relation")
	public R attrNoRelation(@PathVariable(value = "attrGroupId") Long attrGroupId, @RequestParam Map<String, Object> param) {
		PageUtils noRelationAttr = attrGroupService.getNoRelationAttr(param, attrGroupId);
		return R.ok().put("page", noRelationAttr);
	}
	
	/**
	 * @Description 保存关联属性
	 * @author Smile
	 * @date 2021/1/24/024
	 * @param relationVos 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/attr/relation")
	public R addRelation(@RequestBody List<AttrGroupRelationVo> relationVos) {
		attrAttrgroupRelationService.saveBatch(relationVos);
		return R.ok();
	}
	
	
	/**
	 * @param vos 1
	 * @return cn.smile.common.utils.R
	 * @Description 批量删除
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@PostMapping("/attr/relation/delete")
	public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos) {
		attrGroupService.deleteRelation(vos);
		return R.ok();
	}
	
	/**
	 * @param catId  1
	 * @param params 2
	 * @return cn.smile.common.utils.R
	 * @Description 根据分类id获取对应分类下所有的属性分组
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@GetMapping("/list/{catId}")
	public R list(@PathVariable(value = "catId") Long catId,
				  @RequestParam Map<String, Object> params) {
		PageUtils pageUtils = attrGroupService.queryPage(params, catId);
		return R.ok().put("page", pageUtils);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{attrGroupId}")
	public R info(@PathVariable("attrGroupId") Long attrGroupId) {
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
		Long[] categoryLogPath = categoryService.findCategoryLogPath(attrGroup.getCatelogId());
		attrGroup.setCateLogPath(categoryLogPath);
		return R.ok().put("attrGroup", attrGroup);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody AttrGroupEntity attrGroup) {
		attrGroupService.save(attrGroup);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody AttrGroupEntity attrGroup) {
		attrGroupService.updateById(attrGroup);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] attrGroupIds) {
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
		
		return R.ok();
	}
	
}
