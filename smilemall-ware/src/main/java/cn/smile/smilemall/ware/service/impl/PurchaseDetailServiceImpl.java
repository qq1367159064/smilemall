package cn.smile.smilemall.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.ware.dao.PurchaseDetailDao;
import cn.smile.smilemall.ware.entity.PurchaseDetailEntity;
import cn.smile.smilemall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();
        
        String key = !StringUtils.isEmpty(params.get("key").toString()) ? params.get("key").toString() : null;
        if(!StringUtils.isEmpty(key)) {
            purchaseDetailEntityQueryWrapper.and(w -> {
                w.eq("purchase_id", key).or().eq("sku_id", key);
            });
        }
        
        Integer status = !StringUtils.isEmpty(params.get("status").toString()) ? Integer.parseInt(params.get("status").toString()):
                null;
        
        if (status != null) {
            purchaseDetailEntityQueryWrapper.eq("status", status);
        }
    
        Integer wareId = !StringUtils.isEmpty(params.get("wareId").toString()) ?
                Integer.parseInt(params.get("wareId").toString()): null;
        
        if(wareId != null) {
            purchaseDetailEntityQueryWrapper.eq("wareId", wareId);
        }
        
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                purchaseDetailEntityQueryWrapper
                
        );

        return new PageUtils(page);
    }
    
    /**
     * @Description 根据采购单id更新采购项
     * @author Smile
     * @date 2021/1/28/028
     * @param id 1
     * @return java.util.List<cn.smile.smilemall.ware.entity.PurchaseDetailEntity>
     */
    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        return this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
    }
}