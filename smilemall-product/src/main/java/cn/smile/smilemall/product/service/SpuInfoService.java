package cn.smile.smilemall.product.service;

import cn.smile.smilemall.product.entity.SpuInfoDescEntity;
import cn.smile.smilemall.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
	void saveInfo(SpuSaveVo spuSaveVo);
	
	void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);
	
	void saveSpuInfoDesc(SpuInfoDescEntity descEntity);
	
	PageUtils queryPageByCondition(Map<String, Object> params);
	
	boolean up(Long up);
	
	/**
	 * <p></p>
	 * @author smile
	 * @date 2021/3/3/003
	 * @param skuId 1
	 * @return cn.smile.smilemall.product.entity.SpuInfoEntity
	 */
	SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

