package cn.smile.smilemall.ware.service.impl;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.common.utils.R;
import cn.smile.smilemall.ware.dao.WareInfoDao;
import cn.smile.smilemall.ware.entity.WareInfoEntity;
import cn.smile.smilemall.ware.feign.MemberFeignService;
import cn.smile.smilemall.ware.service.WareInfoService;
import cn.smile.smilemall.ware.vo.FareVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {
    
    @Autowired
    private MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        
        String key = params.get("key") != null ? params.get("key").toString() : null;
        QueryWrapper<WareInfoEntity> wareInfoEntityQueryWrapper = new QueryWrapper<>();
    
        if (!StringUtils.isEmpty(key)) {
            wareInfoEntityQueryWrapper.and(w ->{
                w.eq("id", key).or().like("name", key);
            });
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wareInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }
    
    @Override
    public FareVo getFare(Long adrId) {
        R info = memberFeignService.info(adrId);
        FareVo fareVo = new FareVo();
        Map address = (Map) info.get("memberReceiveAddress");
        if(address != null) {
            String phone = address.get("phone").toString();
            String fare = phone.substring(phone.length() - 1, phone.length());
            fareVo.setAddress(address);
            fareVo.setPrice(fare);
            return fareVo;
        }
        return null;
    }
}