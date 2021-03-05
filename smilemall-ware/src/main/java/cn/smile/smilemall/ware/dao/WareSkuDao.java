package cn.smile.smilemall.ware.dao;

import cn.smile.smilemall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
	 * <p>采购确认完成后入库成功采购的商品</p>
	 * @param skuId  商品项id
	 * @param wareId 仓库id
	 * @param skuNum 数量
	 * @return boolean
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	boolean addStock(@Param(value = "skuId") Long skuId, @Param(value = "wareId") Long wareId,
					 @Param(value = "skuNum") Integer skuNum);
	
	/**
	 * <p>判断是否有库存</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param ids 1
	 * @return boolean
	 */
	Long getSkuHasStock(Long ids);
	
	/**
	 * <p>获取sku所在的仓库</p>
	 * @author smile
	 * @date 2021/3/3/003
	 * @param skuId 1
	 * @return java.util.List<java.lang.Long>
	 */
	List<Long> listWareIdHasSkuStock(Long skuId);
	
	/**
	 * <p>sku的库存锁定</p>
	 * @author smile
	 * @date 2021/3/3/003
	 * @param skuId 1
	 * @param wareId 2
	 * @param num 3
	 * @return boolean
	 */
	boolean lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);
}
