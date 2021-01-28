package cn.smile.smilemall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
	
	boolean addStock(Long skuId, Long wareId, Integer skuNum);
}

