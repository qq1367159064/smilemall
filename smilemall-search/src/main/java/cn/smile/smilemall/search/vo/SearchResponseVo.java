package cn.smile.smilemall.search.vo;

import cn.smile.common.to.es.SkuEsModule;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-18
 */
public class SearchResponseVo {
	
	private Integer pageNum;
	private Long total;
	private Integer totalPages;
	private List<String> pageNavs;
	private List<Long> attrIds = new ArrayList<>();
	
	public List<Long> getAttrIds() {
		return attrIds;
	}
	
	public void setAttrIds(List<Long> attrIds) {
		this.attrIds = attrIds;
	}
	
	private List<SkuEsModule> skuEsModules;
	private List<BrandVo> brandVos;
	private List<AttrVo> attrVos;
	private List<CategoryVo> categoryVos;
	
	private List<NavsVo> navs = new ArrayList<>();
	
	public List<NavsVo> getNavs() {
		return navs;
	}
	
	@Data
	public static class NavsVo {
		private String navName;
		private String navValue;
		private String link;
	}
	
	public void setNavs(List<NavsVo> navs) {
		this.navs = navs;
	}
	
	public Integer getPageNum() {
		return pageNum;
	}
	
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
	public Long getTotal() {
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}
	
	public Integer getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
		this.buildPageNavs(totalPages);
	}
	
	public List<String> getPageNavs() {
		return pageNavs;
	}
	
	public void setPageNavs(List<String> pageNavs) {
		this.pageNavs = pageNavs;
	}
	
	@JsonIgnore
	public void buildPageNavs(long totalPages) {
		if (totalPages <= 0 || totalPages == 1) {
			this.setPageNavs(new ArrayList<>());
			return;
		}
		int step = 3;
		StringBuffer stringBuffer = new StringBuffer();
		
		if (this.getPageNum() > 1) {
			String upPage = "<a href='#' pn=" + (this.getPageNum() - 1) + " class='page_navs'><上一页</a>";
			stringBuffer.append(upPage);
		}
		
		int start = this.pageNum - step <= 0 ? 1 : this.pageNum - step;
		int end = this.pageNum + step > this.totalPages ? this.totalPages : this.pageNum + step;
		
		if (step < this.totalPages) {
			if (start > step) {
				for (int i = 1; i < step; i++) {
					stringBuffer.append("<a href='javascript:void();' pn=" + i + " class='page_navs'>" + i + "</a>");
				}
				stringBuffer.append("<a href='javascript:void();'  style='border: 0;font-size: 20px;color: #999;background: #fff'>...</a>");
			} else {
				for (int i = 1; i < start; i++) {
					if (i == this.pageNum) {
						stringBuffer.append("<a href='javascript:void();' pn=" + i + " class='page_navs' style='border: 0;color:#ee2222;background: #fff'>" + i + "</a>");
					} else {
						stringBuffer.append("<a href='javascript:void();' pn=" + i + " class='page_navs'>" + i + "</a>");
					}
				}
			}
		}
		for (int i = start; i <= end; i++) {
			if (i == this.pageNum) {
				stringBuffer.append("<a href='javascript:void();' pn=" + i + " class='page_navs' style='border: 0;color:#ee2222;background: #fff' >" + i + "</a>");
			} else {
				stringBuffer.append("<a href='javascript:void();' pn=" + i + " class='page_navs'>" + i + "</a>");
			}
		}
		if (end < this.totalPages) {
			stringBuffer.append("<a href='javascript:void();'  style='border: 0;font-size: 20px;color: #999;background: " +
					"#fff'>...</a>");
		}
		
		if (this.getPageNum() < this.getTotalPages()) {
			String nextPage = "<a href='#' pn=" + (this.getPageNum() + 1) + " class='page_navs'><下一页</a>";
			stringBuffer.append(nextPage);
		}
		this.setPageNavs(Arrays.asList(stringBuffer.toString()));
	}
	
	public List<SkuEsModule> getSkuEsModules() {
		return skuEsModules;
	}
	
	public void setSkuEsModules(List<SkuEsModule> skuEsModules) {
		this.skuEsModules = skuEsModules;
	}
	
	public List<BrandVo> getBrandVos() {
		return brandVos;
	}
	
	public void setBrandVos(List<BrandVo> brandVos) {
		this.brandVos = brandVos;
	}
	
	public List<AttrVo> getAttrVos() {
		return attrVos;
	}
	
	public void setAttrVos(List<AttrVo> attrVos) {
		this.attrVos = attrVos;
	}
	
	public List<CategoryVo> getCategoryVos() {
		return categoryVos;
	}
	
	public void setCategoryVos(List<CategoryVo> categoryVos) {
		this.categoryVos = categoryVos;
	}
	
	@Data
	public static class BrandVo {
		private String brandName;
		private String brandImgName;
		private Long brandId;
	}
	
	@Data
	public static class CategoryVo {
		private Long catalogId;
		private String catalogName;
	}
	
	@Data
	public static class AttrVo {
		private Long attrId;
		private String attrName;
		private List<String> attrValues;
	}
	
}
