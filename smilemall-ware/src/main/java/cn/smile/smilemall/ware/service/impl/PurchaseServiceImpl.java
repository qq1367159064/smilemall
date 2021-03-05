package cn.smile.smilemall.ware.service.impl;

import cn.smile.common.constant.WareConstant;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.ware.dao.PurchaseDao;
import cn.smile.smilemall.ware.entity.PurchaseDetailEntity;
import cn.smile.smilemall.ware.entity.PurchaseEntity;
import cn.smile.smilemall.ware.service.PurchaseDetailService;
import cn.smile.smilemall.ware.service.PurchaseService;
import cn.smile.smilemall.ware.service.WareSkuService;
import cn.smile.smilemall.ware.vo.MergeVo;
import cn.smile.smilemall.ware.vo.PurchaseDoneItemsVo;
import cn.smile.smilemall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }
    
    /**
     * @Description 查询没有分配的采购单
     * @author Smile
     * @date 2021/1/28/028
     * @param params 1
     * @return void
     */
    @Override
    @Transactional(rollbackFor = { Exception.class })
    public PageUtils queryPageUnReceive(Map<String, Object> params) {
    
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).or().eq("status", 0);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }
    
    /**
     * @Description 合并采购单
     * @author Smile
     * @date 2021/1/28/028
     * @param mergeVo 1
     * @return boolean
     */
    @Override
    public boolean setMergePurchase(MergeVo mergeVo) {
        // TODO 判断采购单和采购项是否可以进行和平操作
        
        // 判断是否有采购单
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATES.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        // 构建采购项(采购需求)
        List<Long> items = mergeVo.getItems();
        // 采购单id
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailEnum.ASSIDNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
        return true;
    }
    
    /**
     * @Description 模拟 领取采购单
     * @author Smile
     * @date 2021/1/28/028
     * @param ids { 采购单id}
     * @return boolean
     */
    @Override
    public boolean received(List<Long> ids) {
        // 1. 确定单前采购单没有被领取，并且是分配给自己的
        List<PurchaseEntity> updPurchase = ids.stream().map(item -> {
            PurchaseEntity purchaseEntity = this.getById(item);
            return purchaseEntity;
        }).filter(f -> {
            if (f.getStatus() == WareConstant.PurchaseStatusEnum.CREATES.getCode() || f.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            } else {
                return false;
            }
        }).map(m -> {
           m.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
           m.setUpdateTime(new Date());
           return m;
        }).collect(Collectors.toList());
        // 2. 改变采购单的状态
        this.updateBatchById(updPurchase);
        // 3. 改变采购项的状态
        updPurchase.forEach(item -> {
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> updPurchaseDetail = purchaseDetailEntities.stream().map(itex -> {
                itex.setStatus(WareConstant.PurchaseDetailEnum.BUYING.getCode());
                return itex;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(updPurchaseDetail);
        });
        return true;
    }
    
    /**
     * @Description 确认完成采购
     * @author Smile
     * @date 2021/1/28/028
     * @param purchaseDoneVo 1
     * @return boolean
     */
    @Override
    public boolean done(PurchaseDoneVo purchaseDoneVo) {
        // 获取采购单id
        Long purchaseId = purchaseDoneVo.getPurchaseId();
        // 获取所有传来采购项信息
        List<PurchaseDoneItemsVo> purchaseDoneItemsVos = purchaseDoneVo.getPurchaseDoneItemsVos();
        List<PurchaseDetailEntity> updatePurchaseDetail = new ArrayList<>();
        Boolean flag = false;
        // 确认采购项的状态
        for (PurchaseDoneItemsVo purchaseDoneItemsVo : purchaseDoneItemsVos) {
            // 获取采购需求
            PurchaseDetailEntity updP = new PurchaseDetailEntity();
            if(purchaseDoneItemsVo.getStatus() == WareConstant.PurchaseDetailEnum.HASERROR.getCode()) {
                flag = true;
                updP.setStatus(WareConstant.PurchaseDetailEnum.HASERROR.getCode());
            } else {
                updP.setStatus(WareConstant.PurchaseDetailEnum.FINISH.getCode());
                // 成功入ku
                PurchaseDetailEntity detailEntity = purchaseDetailService.getById(purchaseDoneItemsVo.getItemId());
                wareSkuService.addStock(detailEntity.getSkuId(), detailEntity.getWareId(), detailEntity.getSkuNum());
            }
            updP.setId(purchaseDoneItemsVo.getItemId());
            updP.setPurchaseId(purchaseId);
            updatePurchaseDetail.add(updP);
        }
        // 更新
        purchaseDetailService.updateBatchById(updatePurchaseDetail);
    
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        if(!flag) {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISH.getCode());
        } else {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        }
        this.updateById(purchaseEntity);
        return true;
    }
}