package cn.smile.smilemall.search.services;

import cn.smile.common.to.es.SkuEsModule;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-15
 */
public interface ProductServices {
	
	boolean productStatusUp(List<SkuEsModule> skuEsModules);
}
