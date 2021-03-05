package cn.smile.smilemall.cart.smilemallcart.controller;

import cn.smile.smilemall.cart.smilemallcart.interceptor.CartInterceptor;
import cn.smile.smilemall.cart.smilemallcart.services.CartService;
import cn.smile.smilemall.cart.smilemallcart.to.UserInfoTo;
import cn.smile.smilemall.cart.smilemallcart.vo.CartItemVo;
import cn.smile.smilemall.cart.smilemallcart.vo.CartVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-25
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
	
	private final CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@ResponseBody
	@GetMapping("/getCartHasCheck")
	public List<CartItemVo> getCartHasCheck(@RequestAttribute(CartInterceptor.REQUEST_USERINFO) UserInfoTo userInfoTo) {
		return cartService.getCartHasCheck(userInfoTo);
	}
	
	@GetMapping("cart.html")
	public String cartListPage(HttpServletRequest request, Model model) {
		UserInfoTo userInfoTo = (UserInfoTo) request.getAttribute(CartInterceptor.REQUEST_USERINFO);
		CartVo cartVo = cartService.getCartInfo(userInfoTo);
		model.addAttribute("cartVo", cartVo);
		model.addAttribute("userId", userInfoTo.getUserId() != null ? userInfoTo.getUserId() : null);
		return "cartList";
	}
	
	@GetMapping("/addToCart")
	public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("number") Integer number, RedirectAttributes model,
							@RequestAttribute(CartInterceptor.REQUEST_USERINFO) UserInfoTo userInfoTo) {
		cartService.addCart(skuId, number, userInfoTo);
		model.addAttribute("skuId", skuId);
		return "redirect:http://cart.smilemall.cn/cart/addCartToSuccess.html";
	}
	
	@GetMapping("/addCartToSuccess.html")
	public String addToSuccess(@RequestParam("skuId") Long skuId,
							   @RequestAttribute(CartInterceptor.REQUEST_USERINFO) UserInfoTo userInfoTo, Model model) {
		CartItemVo cartItem = cartService.getCartItem(skuId, userInfoTo);
		model.addAttribute("skuInfo", cartItem);
		return "success";
	}
	
	@GetMapping("/checkItem")
	public String checkItem(@RequestParam(value = "skuId") Long skuId,
							@RequestParam(value = "check") int check,
							@RequestAttribute(CartInterceptor.REQUEST_USERINFO) UserInfoTo userInfoTo) {
		cartService.checkItem(skuId, check, userInfoTo);
		return "redirect:http://cart.smilemall.cn/cart/cart.html";
	}
	
	@GetMapping("/countItem")
	public String countItem(@RequestParam(value = "skuId") Long skuId,
							@RequestParam(value = "num") int num,
							@RequestAttribute(CartInterceptor.REQUEST_USERINFO) UserInfoTo userInfoTo) {
		num = num <= 0 ? 1 : num;
		cartService.countItem(skuId, num, userInfoTo);
		return "redirect:http://cart.smilemall.cn/cart/cart.html";
	}
	
	@GetMapping("/deleteItem")
	public String deleteItem(@RequestParam(value = "skuId") Long skuId,
							 @RequestAttribute(CartInterceptor.REQUEST_USERINFO) UserInfoTo userInfoTo) {
		cartService.deleteItem(skuId,userInfoTo);
		return "redirect:http://cart.smilemall.cn/cart/cart.html";
	}
}
