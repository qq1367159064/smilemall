package cn.smile.smilemall.ware.feign;

import cn.smile.common.utils.R;
import org.springframework.stereotype.Service;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/28/028
 */
@Service
public class ProdcutFeignImpl implements ProdcutFeign {
	@Override
	public R info(Long skuId) {
		return R.error();
	}
}
