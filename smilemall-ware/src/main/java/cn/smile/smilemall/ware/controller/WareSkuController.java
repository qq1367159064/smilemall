package cn.smile.smilemall.ware.controller;

import cn.smile.common.exception.BizCodeEnume;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;
import cn.smile.smilemall.ware.entity.WareSkuEntity;
import cn.smile.smilemall.ware.exception.NotStockException;
import cn.smile.smilemall.ware.service.WareSkuService;
import cn.smile.smilemall.ware.vo.SkuHasStockVo;
import cn.smile.smilemall.ware.vo.WareSkuLockVo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品库存
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    private final WareSkuService wareSkuService;
    
    public WareSkuController(WareSkuService wareSkuService) {
        this.wareSkuService = wareSkuService;
    }
    
    @PostMapping("/orderLockStock")
    public R orderLockStock(@RequestBody WareSkuLockVo wareSkuLockVo) {
        Boolean aBoolean = false;
        try {
            aBoolean = wareSkuService.orderLockStock(wareSkuLockVo);
            if (aBoolean) {
                return R.ok();
            }
        } catch (NotStockException e) {
            return R.error(BizCodeEnume.NOT_STOCK.getCode(), BizCodeEnume.NOT_STOCK.getMsg());
        }
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
    
    
    /**
     * <p>判断是否有库存</p>
     * @author Smile
     * @date 2021/2/15/015
     * @param ids 1
     * @return cn.smile.common.utils.R
     */
    @PostMapping("/skuHasStock")
    public R getSkuHasStock(@RequestBody List<Long> ids) {
        List<SkuHasStockVo> skuHasStock = wareSkuService.getSkuHasStock(ids);
        return R.ok().put("skuHasStock", skuHasStock);
    }
    
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);
        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
