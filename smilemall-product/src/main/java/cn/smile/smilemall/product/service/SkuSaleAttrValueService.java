package cn.smile.smilemall.product.service;

import cn.smile.smilemall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.SkuSaleAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

	/**
	 * <p>通用分页查询</p>
	 * @author smile
	 * @date 2021/2/26/026
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 */
    PageUtils queryPage(Map<String, Object> params);
    
    List<SkuItemVo.ItemSaleAttrVo> selectSaleBySpuId(Long spuId);
	
    /**
     * <p>根据skuId返回所对应的值</p>
     * @author smile
     * @date 2021/2/26/026
     * @param skuId 1
     * @return java.util.List<java.lang.String>
     */
	List<String> getSkuSaleAttrValue(Long skuId);
}

