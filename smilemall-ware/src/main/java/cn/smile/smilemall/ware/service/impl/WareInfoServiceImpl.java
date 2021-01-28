package cn.smile.smilemall.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.ware.dao.WareInfoDao;
import cn.smile.smilemall.ware.entity.WareInfoEntity;
import cn.smile.smilemall.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

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

}