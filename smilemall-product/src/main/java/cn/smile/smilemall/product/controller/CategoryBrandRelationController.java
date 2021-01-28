package cn.smile.smilemall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import cn.smile.smilemall.product.entity.BrandEntity;
import cn.smile.smilemall.product.vo.BrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.smile.smilemall.product.entity.CategoryBrandRelationEntity;
import cn.smile.smilemall.product.service.CategoryBrandRelationService;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    
    /**
     * @Description 获取对应分类下的所有品牌信息
     * @author Smile
     * @date 2021/1/24/024
     * @param catId 1
     * @return cn.smile.common.utils.R
     */
    @GetMapping("/brands/list")
    public R getCategoryBrand(@RequestParam(value = "catId") Long catId) {
        List<BrandEntity> brandEntities = categoryBrandRelationService.queryCategoryBrand(catId);
        List<BrandVo> collect = brandEntities.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data", collect);
    
    }
    
    

    /**
     * 列表
     */
    @RequestMapping("/list")

    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")

    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")

    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")

    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")

    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    
    /**
     * @Description 获取对应Id品牌的关联分类列表
     * @author Smile
     * @date 2021/1/15/015
     * @param brandId
     * @return cn.smile.common.utils.R
     */
    @GetMapping("/catelog/list")
    public R categoryLogList(@RequestParam("brandId") Long brandId) {
        List<CategoryBrandRelationEntity> data =
                categoryBrandRelationService.list(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
        return R.ok().put("page", data);
    }

}
