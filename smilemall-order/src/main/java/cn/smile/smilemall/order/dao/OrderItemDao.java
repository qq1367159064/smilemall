package cn.smile.smilemall.order.dao;

import cn.smile.smilemall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:37:54
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
