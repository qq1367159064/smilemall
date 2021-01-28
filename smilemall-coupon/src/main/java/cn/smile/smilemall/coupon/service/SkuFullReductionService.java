package cn.smile.smilemall.coupon.service;

import cn.smile.common.to.SkuReductionTo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:29:05
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);
	
	void saveSkuReduction(SkuReductionTo skuReductionTo);
}

