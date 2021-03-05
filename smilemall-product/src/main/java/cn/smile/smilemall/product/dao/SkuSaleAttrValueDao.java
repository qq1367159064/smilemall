package cn.smile.smilemall.product.dao;

import cn.smile.smilemall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {
	
	/**
	 * <p>根据skuid查询sku的属性信息</p>
	 * @author smile
	 * @date 2021/2/26/026
	 * @param skuId 1
	 * @return java.util.List<java.lang.String>
	 */
	List<String> selectSkuSaleAttrValue(Long skuId);
}
