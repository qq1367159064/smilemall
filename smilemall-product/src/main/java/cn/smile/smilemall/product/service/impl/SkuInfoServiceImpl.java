package cn.smile.smilemall.product.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.product.dao.SkuInfoDao;
import cn.smile.smilemall.product.entity.SkuInfoEntity;
import cn.smile.smilemall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }
    
    /**
     * @Description 检索sku
     * @author Smile
     * @date 2021/1/27/027
     * @param params 1
     * @return cn.smile.common.utils.PageUtils
     */
    @Override
    public PageUtils queryPageCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<>();
        
        String key = params.get("key") != null ? params.get("key").toString() : "";
        if(!StringUtils.isEmpty(key)) {
            skuInfoEntityQueryWrapper.and(w -> {
                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        Integer catelogid = params.get("catelogId") != null ? Integer.parseInt(params.get("catelogId").toString()) : null;
        if(!StringUtils.isEmpty(catelogid) && catelogid != 0) {
            skuInfoEntityQueryWrapper.eq("catelog_id", catelogid);
        }
    
        Integer brandId = params.get("brandId") != null ? Integer.parseInt(params.get("brandId").toString()) : null;
        if(!StringUtils.isEmpty(brandId) && brandId != 0) {
            skuInfoEntityQueryWrapper.eq("brand_id", brandId);
        }
    
        BigDecimal min = params.get("min") != null ? new BigDecimal(Double.parseDouble(params.get("min").toString())) : null;
        if(!StringUtils.isEmpty(min) && min.compareTo(new BigDecimal(0)) == 1) {
            skuInfoEntityQueryWrapper.ge("price", min.intValue());
        }
    
        BigDecimal max = params.get("max") != null ? new BigDecimal(Double.parseDouble(params.get("max").toString())) : null;
        if(!StringUtils.isEmpty(max) && max.compareTo(new BigDecimal(0)) == 1) {
            skuInfoEntityQueryWrapper.eq("price", max.intValue());
        }
        
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                skuInfoEntityQueryWrapper
        );
        return new PageUtils(page);
    }
}