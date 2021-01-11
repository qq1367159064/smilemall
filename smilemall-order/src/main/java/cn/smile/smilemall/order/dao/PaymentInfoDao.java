package cn.smile.smilemall.order.dao;

import cn.smile.smilemall.order.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付信息表
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:37:54
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
