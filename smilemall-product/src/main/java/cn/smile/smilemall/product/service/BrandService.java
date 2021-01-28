package cn.smile.smilemall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    int updateDetail(BrandEntity brandEntity);
}

