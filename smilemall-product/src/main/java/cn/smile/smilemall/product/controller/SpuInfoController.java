package cn.smile.smilemall.product.controller;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;
import cn.smile.smilemall.product.entity.SpuInfoEntity;
import cn.smile.smilemall.product.service.SpuInfoService;
import cn.smile.smilemall.product.vo.SpuSaveVo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * spu信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    private final SpuInfoService spuInfoService;
    
    public SpuInfoController(SpuInfoService spuInfoService) {
        this.spuInfoService = spuInfoService;
    }
    
    
    /**
     * <p>商品上架</p>
     * @author Smile
     * @date 2021/2/14/014
     * @param spuId 1
     * @return cn.smile.common.utils.R
     */
    @PostMapping("/{spuId}/up")
    public R up(@PathVariable(value = "spuId") Long spuId) {
        if (spuInfoService.up(spuId)) {
            return R.ok();
        }
        return R.error();
    }
    
    
    @GetMapping("/getSpuInfoBySkuId")
    public R getSpuInfoBySkuId(@PathVariable(value = "skuId") Long skuId) {
        SpuInfoEntity spuInfo = spuInfoService.getSpuInfoBySkuId(skuId);
        return R.ok().put("spuInfo", spuInfo);
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
        spuInfoService.saveInfo(spuSaveVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
