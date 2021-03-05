package cn.smile.smilemall.product.controller;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;
import cn.smile.smilemall.product.entity.SkuSaleAttrValueEntity;
import cn.smile.smilemall.product.service.SkuSaleAttrValueService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * sku销售属性&值
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@RestController
@RequestMapping("product/skusaleattrvalue")
public class SkuSaleAttrValueController {
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    
    public SkuSaleAttrValueController(SkuSaleAttrValueService skuSaleAttrValueService) {
        this.skuSaleAttrValueService = skuSaleAttrValueService;
    }
    
    
    @GetMapping("/getSkuSaleAttrValue/{skuId}")
    public List<String> getSkuSaleAttrValue(@PathVariable("skuId") Long skuId) {
        return skuSaleAttrValueService.getSkuSaleAttrValue(skuId);
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuSaleAttrValueService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SkuSaleAttrValueEntity skuSaleAttrValue = skuSaleAttrValueService.getById(id);

        return R.ok().put("skuSaleAttrValue", skuSaleAttrValue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.save(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.updateById(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		skuSaleAttrValueService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
