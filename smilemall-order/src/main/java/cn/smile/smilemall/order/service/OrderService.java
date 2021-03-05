package cn.smile.smilemall.order.service;

import cn.smile.smilemall.order.vo.OrderConfirmVo;
import cn.smile.smilemall.order.vo.OrderSubmitVo;
import cn.smile.smilemall.order.vo.SubmitOrderResponseVo;
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

	/**
	 * <p>通用分页查询</p>
	 * @author smile
	 * @date 2021/3/2/002
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 */
    PageUtils queryPage(Map<String, Object> params);
	
    /**
     * <p>返回订单信息</p>
     * @author smile
     * @date 2021/2/28/028
     * @return cn.smile.smilemall.order.vo.OrderConfirmVo
     */
	OrderConfirmVo confirmOrder();
	
	/**
	 * <p>订单构建</p>
	 * @author smile
	 * @date 2021/3/2/002
	 * @param orderSubmitVo 1
	 * @return cn.smile.smilemall.order.vo.SubmitOrderResponseVo
	 */
	SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo);
}

