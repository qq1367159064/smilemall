package cn.smile.smilemall.order.web.controller;

import cn.smile.smilemall.order.service.OrderService;
import cn.smile.smilemall.order.vo.OrderConfirmVo;
import cn.smile.smilemall.order.vo.OrderSubmitVo;
import cn.smile.smilemall.order.vo.SubmitOrderResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-28
 */
@Controller
public class OrderPageController {

	private final OrderService orderService;
	
	public OrderPageController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@GetMapping("/orderList.html")
	public String toOrderListPage() {
		return "list";
	}
	
	@GetMapping("/confirm.html")
	public String toConfirmPage(Model model) {
		OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
		model.addAttribute("orderConfirmVo", orderConfirmVo);
		return "confirm";
	}
	
	@GetMapping("/play.html")
	public String toPlayPage() {
		return "play";
	}
	
	
	@PostMapping("/submitOrder")
	public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes) {
		SubmitOrderResponseVo submitOrderResponseVo = orderService.submitOrder(orderSubmitVo);
		if (submitOrderResponseVo.getCode() != 0) {
			String msg = "下单失败: ";
			switch (submitOrderResponseVo.getCode()) {
				case 1: msg += "订单信息过期，请刷新页面";break;
				case 2: msg += "订单商品价格发生变化请确认后提交";break;
				case 3: msg += "商品库存不足";break;
				default: msg += "未知错误";
				
			}
			redirectAttributes.addFlashAttribute("err", msg);
			return "redirect:http://order.smilemall.cn/confirm.html";
		}
		model.addAttribute("submitOrderResp", submitOrderResponseVo);
		return "play";
	}
}
