package cn.smile.smilemall.coupon.service.impl;

import cn.smile.common.to.MemberPrice;
import cn.smile.common.to.SkuReductionTo;
import cn.smile.smilemall.coupon.entity.MemberPriceEntity;
import cn.smile.smilemall.coupon.entity.SkuLadderEntity;
import cn.smile.smilemall.coupon.service.MemberPriceService;
import cn.smile.smilemall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.coupon.dao.SkuFullReductionDao;
import cn.smile.smilemall.coupon.entity.SkuFullReductionEntity;
import cn.smile.smilemall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    private SkuLadderService skuLadderService;
    
    @Autowired
    private MemberPriceService memberPriceService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }
    
    /**
     * @Description 保存优惠信息
     * @author Smile
     * @date 2021/1/25/025
     * @param skuReductionTo 1
     * @return void
     */
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 保存sku的优惠价格
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuLadderEntity.getSkuId());
        skuLadderEntity.setFullCount(skuLadderEntity.getFullCount());
        skuLadderEntity.setDiscount(skuLadderEntity.getDiscount());
        skuLadderEntity.setAddOther(skuLadderEntity.getAddOther());
        if (skuReductionTo.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }
        
        // 保存满减信息
        if(skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1) {
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            skuFullReductionEntity.setSkuId(skuReductionTo.getSkuId());
            BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
            this.save(skuFullReductionEntity);
        }
        
        
        // 保存会员价格
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> m = memberPrice.stream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter(item -> item.getMemberPrice().compareTo(new BigDecimal(0)) == 1).collect(Collectors.toList());
        memberPriceService.saveBatch(m);
    }
}