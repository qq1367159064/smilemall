package cn.smile.smilemall.ware.service.impl;

import cn.smile.common.utils.R;
import cn.smile.smilemall.ware.feign.ProdcutFeign;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.ware.dao.WareSkuDao;
import cn.smile.smilemall.ware.entity.WareSkuEntity;
import cn.smile.smilemall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
	@Autowired
	private ProdcutFeign prodcutFeign;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		
		QueryWrapper<WareSkuEntity> wareSkuEntityQueryWrapper = new QueryWrapper<>();
		Integer skuId = !StringUtils.isEmpty(params.get("skuId")) ? Integer.parseInt(params.get("skuId").toString()) : null;
		if (!StringUtils.isEmpty(skuId)) {
			wareSkuEntityQueryWrapper.eq("sku_id", skuId);
		}
		Integer wareId = !StringUtils.isEmpty(params.get("wareId")) ? Integer.parseInt(params.get("wareId").toString()) :
				null;
		if (!StringUtils.isEmpty(skuId)) {
			wareSkuEntityQueryWrapper.eq("ware_id", wareId);
		}
		
		IPage<WareSkuEntity> page = this.page(
				new Query<WareSkuEntity>().getPage(params),
				wareSkuEntityQueryWrapper
		);
		
		return new PageUtils(page);
	}
	
	/**
	 * @param skuId  1
	 * @param wareId 2
	 * @param skuNum 3
	 * @return boolean
	 * @Description 商品入库
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean addStock(Long skuId, Long wareId, Integer skuNum) {
		WareSkuEntity wareSkuEntity = new WareSkuEntity();
		wareSkuEntity.setSkuId(skuId);
		wareSkuEntity.setWareId(wareId);
		wareSkuEntity.setStock(skuNum);
		wareSkuEntity.setStock(0);
		R info = prodcutFeign.info(skuId);
		if (info.getCode() == 0) {
			Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
			wareSkuEntity.setSkuName(skuInfo.get("skuName").toString());
		}
		if (!this.addStock(skuId, wareId, skuNum)) {
			this.save(wareSkuEntity);
		}
		return true;
	}
}