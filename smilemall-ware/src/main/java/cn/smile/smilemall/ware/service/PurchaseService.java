package cn.smile.smilemall.ware.service;

import cn.smile.smilemall.ware.vo.MergeVo;
import cn.smile.smilemall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */

public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
	
	PageUtils queryPageUnReceive(Map<String, Object> params);
	
	boolean setMergePurchase(MergeVo mergeVo);
	
	boolean received(List<Long> ids);
	
	boolean done(PurchaseDoneVo purchaseDoneVo);
}

