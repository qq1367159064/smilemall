package cn.smile.smilemall.product.service;

import cn.smile.smilemall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.SkuInfoEntity;

import java.math.BigDecimal;
import java.util.List;
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
	
	List<SkuInfoEntity> getSkuInfoBySpuId(Long spuId);
	
	SkuItemVo selectSkuItem(Long skuId);
	
	/**
	 * <p>获取商品单价</p>
	 * @author smile
	 * @date 2021/2/28/028
	 * @param skuId 1
	 * @return java.math.BigDecimal
	 */
	BigDecimal getSkuPrice(Long skuId);
}

