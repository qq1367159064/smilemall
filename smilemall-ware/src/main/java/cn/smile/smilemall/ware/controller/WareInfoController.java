package cn.smile.smilemall.ware.controller;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;
import cn.smile.smilemall.ware.entity.WareInfoEntity;
import cn.smile.smilemall.ware.service.WareInfoService;
import cn.smile.smilemall.ware.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 仓库信息
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */
@RestController
@RequestMapping("ware/wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    
    /**
     * <p>获取运费</p>
     * @author smile
     * @date 2021/3/2/002
     * @param adrId 1
     * @return cn.smile.common.utils.R
     */
    
    @GetMapping("/fare")
    public R getFare(@RequestParam("adrId") Long adrId) {
        FareVo fare = wareInfoService.getFare(adrId);
        return R.ok().put("fare", fare);
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
