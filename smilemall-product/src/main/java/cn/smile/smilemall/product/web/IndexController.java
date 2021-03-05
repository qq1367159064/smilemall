package cn.smile.smilemall.product.web;

import cn.smile.smilemall.product.entity.CategoryEntity;
import cn.smile.smilemall.product.service.CategoryService;
import cn.smile.smilemall.product.vo.CategoryLeaveTwoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * <p>页面跳转</p>
 *
 * @author smile
 * @date 2021-02-15
 */
@Controller
public class IndexController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping({"/", "index.html"})
	public String indexPage(Model model) {
		// TODO查询所有一级分类数据
		List<CategoryEntity> leveOneCategory = categoryService.getLeveOneCategory();
		model.addAttribute("category", leveOneCategory);
		return "index";
	}
	
	//===========================================页面数据处理==============================================
	@ResponseBody
	@GetMapping("/index/json/catalog")
	public Map<String, List<CategoryLeaveTwoVo>> getCatalogJson() {
		Map<String, List<CategoryLeaveTwoVo>> catalogJson = categoryService.getCatalogJson();
		return catalogJson;
	}
}
