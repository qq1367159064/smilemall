package cn.smile.smilemall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import cn.smile.common.valid.AddGroup;
import cn.smile.common.valid.UpdateGroup;
import cn.smile.common.valid.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.smile.smilemall.product.entity.BrandEntity;
import cn.smile.smilemall.product.service.BrandService;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
	@Autowired
	private BrandService brandService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
//    @RequiresPermissions("product:brand:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = brandService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{brandId}")
//    @RequiresPermissions("product:brand:info")
	public R info(@PathVariable("brandId") Long brandId) {
		BrandEntity brand = brandService.getById(brandId);
		return R.ok().put("brand", brand);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
//    @RequiresPermissions("product:brand:save")
	public R save(@Validated(value = { AddGroup.class }) @RequestBody BrandEntity brand) {
		brandService.save(brand);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
//    @RequiresPermissions("product:brand:update")
	public R update(@Validated(value = {UpdateGroup.class })@RequestBody BrandEntity brand) {
		brandService.updateDetail(brand);
		
		return R.ok();
	}
	
	/**
	 * @Description 修改品牌的状态
	 * @author Smile
	 * @date 2021/1/15/015
	 * @param brandEntity 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("update/status")
	public R updateStatus(@Validated(value = {UpdateStatus.class}) @RequestBody BrandEntity brandEntity) {
		brandService.updateById(brandEntity);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
//    @RequiresPermissions("product:brand:delete")
	public R delete(@RequestBody Long[] brandIds) {
		brandService.removeByIds(Arrays.asList(brandIds));
		return R.ok();
	}
}
