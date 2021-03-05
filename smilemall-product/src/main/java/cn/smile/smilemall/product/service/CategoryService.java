package cn.smile.smilemall.product.service;

import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.CategoryEntity;
import cn.smile.smilemall.product.vo.CategoryLeaveTwoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * <p>通用分页查询</p>
     * @author Smile
     * @date 2021/2/17/017
     * @param params { 页面查询条件和分页页数和显示条数 }
     * @return cn.smile.common.utils.PageUtils
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * <p> 查询所有分类和子分类，以树形的结构组装</p>
     * @author Smile
     * @date 2021/2/17/017
     * @return java.util.List<cn.smile.smilemall.product.entity.CategoryEntity>
     */
    List<CategoryEntity> listWithTree();
    
    /**
     * <p>根据id删除分类 {逻辑删除}</p>
     * @author Smile
     * @date 2021/2/17/017
     * @param menuIds 1
     * @return java.lang.Integer
     */
    Integer removeMenuByIds(List<Long> menuIds);
    
    /**
     * <p>根据分类id获取分类的显示层级</p>
     * @author Smile
     * @date 2021/2/17/017
     * @param cateLogId 1
     * @return java.lang.Long[]
     */
    Long [] findCategoryLogPath(Long cateLogId);

    /**
     * <p>更新分类信息</p>
     * @author Smile
     * @date 2021/2/17/017
     * @param categoryEntity 1
     * @return boolean
     */
    boolean updateDetail(CategoryEntity categoryEntity);
    
    
    /**
     * <p>获取一级分类数据</p>
     * @author Smile
     * @date 2021/2/17/017
     * @return java.util.List<cn.smile.smilemall.product.entity.CategoryEntity>
     */
    List<CategoryEntity> getLeveOneCategory();
    
    /**
     * <p>获取二级分类数据</p>
     * @author Smile
     * @date 2021/2/17/017
     * @return java.util.Map<java.lang.String,java.util.List<cn.smile.smilemall.product.vo.CategoryLeaveTwoVo>>
     */
    Map<String, List<CategoryLeaveTwoVo>> getCatalogJson();
    
}

