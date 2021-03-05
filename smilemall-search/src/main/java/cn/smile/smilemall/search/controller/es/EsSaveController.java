package cn.smile.smilemall.search.controller.es;

import cn.smile.common.exception.BizCodeEnume;
import cn.smile.common.to.es.SkuEsModule;
import cn.smile.common.utils.R;
import cn.smile.smilemall.search.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>保存商品</p>
 *
 * @author smile
 * @date 2021-02-15
 */
@RequestMapping("/search/save")
@RestController
public class EsSaveController {
	@Autowired
	private ProductServices productServices;
	
	/**
	 * <p>商品上架</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param skuEsModules 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/es/productUp")
	public R productStatusUp(@RequestBody List<SkuEsModule> skuEsModules) {
		if (productServices.productStatusUp(skuEsModules)) {
			return R.ok();
		} else {
			return R.error(BizCodeEnume.PRODUCT_UP_ERROR.getCode(), BizCodeEnume.PRODUCT_UP_ERROR.getMsg());
		}
	}
}
