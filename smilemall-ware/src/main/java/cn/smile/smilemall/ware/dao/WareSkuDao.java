package cn.smile.smilemall.ware.dao;

import cn.smile.smilemall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	/**
	 * @param skuId  商品项id
	 * @param wareId 仓库id
	 * @param skuNum 数量
	 * @return boolean
	 * @Description 采购确认完成后入库成功采购的商品
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	boolean addStock(@Param(value = "skuId") Long skuId, @Param(value = "wareId") Long wareId,
					 @Param(value = "skuNum") Long skuNum);
}
