package cn.smile.smilemall.product.service.impl;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.product.dao.SkuImagesDao;
import cn.smile.smilemall.product.entity.SkuImagesEntity;
import cn.smile.smilemall.product.service.SkuImagesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }
    
    @Override
    public List<SkuImagesEntity> selectBySkuImgById(Long skuId) {
        return this.list(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
    }
}