package cn.smile.smilemall.order.dao;

import cn.smile.smilemall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:37:54
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
