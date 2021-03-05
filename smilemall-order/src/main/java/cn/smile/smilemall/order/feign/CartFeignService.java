package cn.smile.smilemall.order.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.smilemall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-28
 */
@SmileFeign
@FeignClient(value = "smilemall-cart")
public interface CartFeignService {
	
	/**
	 * <p>获取购物车信息</p>
	 * @author smile
	 * @date 2021/2/28/028
	 * @return java.util.List<cn.smile.smilemall.order.vo.OrderItemVo>
	 */
	@GetMapping("/cart/getCartHasCheck")
	public List<OrderItemVo> getCartHasCheck();
}
