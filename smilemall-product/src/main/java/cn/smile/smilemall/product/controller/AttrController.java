package cn.smile.smilemall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import cn.smile.smilemall.product.entity.ProductAttrValueEntity;
import cn.smile.smilemall.product.service.ProductAttrValueService;
import cn.smile.smilemall.product.vo.AttrRespVo;
import cn.smile.smilemall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.smile.smilemall.product.entity.AttrEntity;
import cn.smile.smilemall.product.service.AttrService;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;


/**
 * 商品属性
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:09
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
	@Autowired
	private AttrService attrService;
	@Autowired
	private ProductAttrValueService attrValueService;
	
	/**
	 * @param spuId 1
	 * @return cn.smile.common.utils.R
	 * @Description 获取spu的属性
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	@GetMapping("/base/listforspu/{spudId}")
	public R baseAttrList(@PathVariable(value = "spuId") Long spuId) {
		attrValueService.baseListForSpu(spuId);
		return R.ok();
	}
	
	/**
	 * @param spuId 1
	 * @param lists 2
	 * @return cn.smile.common.utils.R
	 * @Description 更新修改属性
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	@PostMapping("/updata/{spuId}")
	public R updSpuAttr(@PathVariable(value = "spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> lists) {
		attrValueService.updateSpuAttr(spuId, lists);
		return R.ok();
	}
	
	/**
	 * @param cateId 1
	 * @param params 2
	 * @return cn.smile.common.utils.R
	 * @Description 根据品牌分类查询分组信息
	 * @author Smile
	 * @date 2021/1/23/023
	 */
	@GetMapping("/{base}/list/{categoryId}")
	public R baseAttrList(@PathVariable(value = "categoryId") Long cateId, @RequestParam Map<String, Object> params,
						  @PathVariable(value = "base") String type) {
		PageUtils pageUtils = attrService.queryBaseAttrPage(params, cateId, type);
		return R.ok().put("page", pageUtils);
	}
	
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
//    @RequiresPermissions("product:attr:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = attrService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{attrId}")
//    @RequiresPermissions("product:attr:info")
	public R info(@PathVariable("attrId") Long attrId) {
		AttrRespVo attrInfo = attrService.getAttrInfo(attrId);
		
		return R.ok().put("attr", attrInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
//    @RequiresPermissions("product:attr:save")
	public R save(@RequestBody AttrVo attr) {
		attrService.saveAttr(attr);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
//    @RequiresPermissions("product:attr:update")
	public R update(@RequestBody AttrVo attr) {
		attrService.updateAttr(attr);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
//    @RequiresPermissions("product:attr:delete")
	public R delete(@RequestBody Long[] attrIds) {
		attrService.removeByIds(Arrays.asList(attrIds));
		
		return R.ok();
	}
	
}
