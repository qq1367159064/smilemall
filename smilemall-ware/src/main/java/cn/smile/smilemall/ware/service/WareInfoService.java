package cn.smile.smilemall.ware.service;

import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.ware.entity.WareInfoEntity;
import cn.smile.smilemall.ware.vo.FareVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:35:52
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
	
    /**
     * <p>根据用户地址计算运费</p>
     * @author smile
     * @date 2021/3/2/002
     * @param adrId 1
     * @return java.math.BigDecimal
     */
	FareVo getFare(Long adrId);
}

