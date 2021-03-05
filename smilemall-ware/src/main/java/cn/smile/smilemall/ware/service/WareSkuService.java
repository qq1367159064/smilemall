package cn.smile.smilemall.ware.service;

import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.ware.entity.WareSkuEntity;
import cn.smile.smilemall.ware.vo.SkuHasStockVo;
import cn.smile.smilemall.ware.vo.WareSkuLockVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */
public interface WareSkuService extends IService<WareSkuEntity> {

	/**
	 * <p>通用查询</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 */
    PageUtils queryPage(Map<String, Object> params);
	
    /**
     * <p>添加库存</p>
     * @author Smile
     * @date 2021/2/15/015
     * @param skuId 1
     * @param wareId 2
     * @param skuNum 3
     * @return boolean
     */
	boolean addStock(Long skuId, Long wareId, Integer skuNum);
	
	/**
	 * <p>判断是否有库存</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param ids 1
	 * @return  List<SkuHasStockVo>
	 */
	List<SkuHasStockVo>  getSkuHasStock(List<Long> ids);
	
	
	Boolean orderLockStock(WareSkuLockVo wareSkuLockVo);
}

