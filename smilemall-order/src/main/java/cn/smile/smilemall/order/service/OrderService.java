package cn.smile.smilemall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:37:54
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

