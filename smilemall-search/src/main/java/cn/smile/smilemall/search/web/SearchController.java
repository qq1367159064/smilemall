package cn.smile.smilemall.search.web;

import cn.smile.smilemall.search.services.MallSearchService;
import cn.smile.smilemall.search.vo.SearchParamVo;
import cn.smile.smilemall.search.vo.SearchResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-18
 */
@Controller
public class SearchController {
	
	private final MallSearchService mallSearchService;
	
	public SearchController(MallSearchService mallSearchService) {
		this.mallSearchService = mallSearchService;
	}
	
	
	@GetMapping("/list.html")
	public String listPage(SearchParamVo searchParamVo, Model model, HttpServletRequest httpServletRequest) {
		String queryString = httpServletRequest.getQueryString();
		searchParamVo.setQueryString(queryString);
		SearchResponseVo search = mallSearchService.search(searchParamVo);
		model.addAttribute("result", search);
		return "list";
	}
}
