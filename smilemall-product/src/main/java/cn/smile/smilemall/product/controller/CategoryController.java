package cn.smile.smilemall.product.controller;

import cn.smile.common.utils.R;
import cn.smile.smilemall.product.entity.CategoryEntity;
import cn.smile.smilemall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 商品三级分类
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     * 查询所有分类和子分类，以树形的结构组装
     */
    @RequestMapping("/list/tree")
//    @RequiresPermissions("product:category:list")
    public R listThree(){
        //TODO
        List<CategoryEntity> categoryEntities = categoryService.listWithTree();
        return R.ok().put("categoryTree", categoryEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
//    @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return R.ok();
    }

    /**
     * @Description 批量更新分类节点信息
     * @author Smile
     * @date 2021/1/10
     * @param entities 1
     * @return cn.smile.common.utils.R
     */
    @PostMapping("/updateSort")
    public R updateSort(@RequestBody List<CategoryEntity> entities) {
        categoryService.updateBatchById(entities);
        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
//    @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
