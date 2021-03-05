package cn.smile.smilemall.product.service.impl;

import cn.smile.common.annotation.cache.UseCache;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.product.dao.SkuSaleAttrValueDao;
import cn.smile.smilemall.product.entity.SkuInfoEntity;
import cn.smile.smilemall.product.entity.SkuSaleAttrValueEntity;
import cn.smile.smilemall.product.service.SkuInfoService;
import cn.smile.smilemall.product.service.SkuSaleAttrValueService;
import cn.smile.smilemall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    
    @Autowired
    private SkuInfoService skuInfoService;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }
    
    @UseCache(cacheKeyPrefix = "sale")
    @Override
    public List<SkuItemVo.ItemSaleAttrVo> selectSaleBySpuId(Long spuId) {
        List<SkuInfoEntity> skuInfo = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
    
        List<Long> skuIds = skuInfo.stream().map(item -> {
            return item.getSkuId();
        }).collect(Collectors.toList());
    
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities =
                list(new QueryWrapper<SkuSaleAttrValueEntity>().in("sku_id", skuIds));
        List<String> attrName = skuSaleAttrValueEntities.stream().map(name -> {
            return name.getAttrName();
        }).distinct().collect(Collectors.toList());
    
        return attrName.stream().map(itex -> {
            SkuItemVo.ItemSaleAttrVo itemSaleAttrVo = new SkuItemVo.ItemSaleAttrVo();
            List<String> values = skuSaleAttrValueEntities.stream().filter(itexa -> itexa.getAttrName().equals(itex))
                    .map(itexb -> itexb.getAttrValue()).distinct().collect(Collectors.toList());
            itemSaleAttrVo.setAttrName(itex);
            List<SkuItemVo.AttrValueWithSkuId> attrValueWithSkuIds = values.stream().map(itey -> {
                SkuItemVo.AttrValueWithSkuId value = new SkuItemVo.AttrValueWithSkuId();
                value.setAttrValue(itey);
                List<Long> valueWithSkuIds =
                        skuSaleAttrValueEntities.stream().filter(iteya -> iteya.getAttrValue().equals(itey))
                                .map(iteyb -> iteyb.getSkuId()).distinct().collect(Collectors.toList());
                value.setSkuId(valueWithSkuIds);
                return value;
            }).collect(Collectors.toList());
            itemSaleAttrVo.setAttrValueWithSkuIds(attrValueWithSkuIds);
            return itemSaleAttrVo;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<String> getSkuSaleAttrValue(Long skuId) {
        baseMapper.selectSkuSaleAttrValue(skuId);
        return null;
    }
}
