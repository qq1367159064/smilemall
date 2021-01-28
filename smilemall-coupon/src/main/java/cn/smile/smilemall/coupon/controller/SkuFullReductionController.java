package cn.smile.smilemall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import cn.smile.common.to.SkuReductionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.smile.smilemall.coupon.entity.SkuFullReductionEntity;
import cn.smile.smilemall.coupon.service.SkuFullReductionService;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;



/**
 * 商品满减信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:29:05
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * @Description 保存sku的优惠信息{运程调用}
     * @author Smile
     * @date 2021/1/25/025
     * @param skuReductionTo 1
     * @return cn.smile.common.utils.R
     */
    @PostMapping("/saveInfo")
    public R saveInfo(@RequestBody  SkuReductionTo skuReductionTo) {
        skuFullReductionService.saveSkuReduction(skuReductionTo);
        return R.ok();
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
