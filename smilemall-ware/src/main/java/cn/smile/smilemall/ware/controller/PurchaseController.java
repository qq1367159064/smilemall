package cn.smile.smilemall.ware.controller;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;
import cn.smile.smilemall.ware.entity.PurchaseEntity;
import cn.smile.smilemall.ware.service.PurchaseService;
import cn.smile.smilemall.ware.vo.MergeVo;
import cn.smile.smilemall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 采购信息
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    
    /**
     * @Description 查询没有分配的采购单
     * @author Smile
     * @date 2021/1/28/028
     * @param params 1
     * @return cn.smile.common.utils.R
     */
    @GetMapping("/unreceive/list")
    public R unReceive(@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = purchaseService.queryPageUnReceive(params);
        return R.ok().put("data", pageUtils);
    }

    /**
     * @Description 合并采购需求
     * @author Smile
     * @date 2021/1/28/028
     * @param mergeVo 1
     * @return cn.smile.common.utils.R
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo) {
        purchaseService.setMergePurchase(mergeVo);
        return R.ok();
    }
    
    
    /**
     * @Description 确认采购完成
     * @author Smile
     * @date 2021/1/28/028
     * @return cn.smile.common.utils.R
     */
    @PostMapping("/done")
    public R done(@RequestBody PurchaseDoneVo purchaseDoneVo) {
        purchaseService.done(purchaseDoneVo);
        return R.ok();
    }
    /**
     * @Description 领取采购单
     * @author Smile
     * @date 2021/1/28/028
     * @return cn.smile.common.utils.R
     */
    @PostMapping("/received")
    public  R received(@RequestBody List<Long> ids) {
        purchaseService.received(ids);
        return R.ok();
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
