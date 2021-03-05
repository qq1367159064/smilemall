package cn.smile.smilemall.product.web;

/**
 * <p>详情页controller</p>
 *
 * @author smile
 * @date 2021-02-22
 */

import cn.smile.smilemall.product.service.SkuInfoService;
import cn.smile.smilemall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {
	
	@Autowired
	private SkuInfoService skuInfoService;
	
	@GetMapping("/{skuId}.html")
	public String skuItem(@PathVariable("skuId") long skuId, Model model) {
		SkuItemVo skuItemVo = skuInfoService.selectSkuItem(skuId);
		model.addAttribute("skuInfo", skuItemVo);
		return "item";
	}
}
