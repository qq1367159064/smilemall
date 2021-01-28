package cn.smile.smilemall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
	
	PageUtils queryPageCondition(Map<String, Object> params);
}

