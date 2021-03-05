package cn.smile.smilemall.product.controller;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;
import cn.smile.smilemall.product.entity.SkuInfoEntity;
import cn.smile.smilemall.product.service.SkuInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * sku信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    private final SkuInfoService skuInfoService;
    
    public SkuInfoController(SkuInfoService skuInfoService) {
        this.skuInfoService = skuInfoService;
    }
    
    @GetMapping("/getSkuPrice/{skuId}")
    public String getSkuPrice(@PathVariable(value = "skuId") Long skuId) {
        return skuInfoService.getSkuPrice(skuId).toString();
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPageCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
