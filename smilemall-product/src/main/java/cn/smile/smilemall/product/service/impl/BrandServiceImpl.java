package cn.smile.smilemall.product.service.impl;

import cn.smile.smilemall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.product.dao.BrandDao;
import cn.smile.smilemall.product.entity.BrandEntity;
import cn.smile.smilemall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = params.get("key").toString();
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)) {
            queryWrapper.eq("brand_id", key).or().like("name", key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }
    
    /**
     * @Description 级联更新相关的属性
     * @author Smile
     * @date 2021/1/15/015
     * @param brandEntity 1
     * @return int
     */
    @Transactional(rollbackFor = { Exception.class })
    @Override
    public int updateDetail(BrandEntity brandEntity) {
        this.updateById(brandEntity);
        if (!StringUtils.isEmpty(brandEntity.getName())) {
            categoryBrandRelationService.updateBrand(brandEntity.getBrandId(), brandEntity.getName());
        }
        //TODO 级联更新关联数据
        return 0;
    }
}