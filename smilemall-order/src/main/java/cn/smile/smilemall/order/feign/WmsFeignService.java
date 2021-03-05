package cn.smile.smilemall.order.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.common.utils.R;
import cn.smile.smilemall.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>库存服务调用</p>
 *
 * @author smile
 * @date 2021-03-02
 */
@SmileFeign
@FeignClient(value = "cn-smile-smilemall-ware")
public interface WmsFeignService {
	
	/**
	 * <p>判断是否有库存</p>
	 * @author smile
	 * @date 2021/3/2/002
	 * @param ids 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/ware/waresku/skuHasStock")
	public R getSkuHasStock(@RequestBody List<Long> ids) ;
	
	/**
	 * <p>获取运费</p>
	 * @author smile
	 * @date 2021/3/2/002
	 * @param adrId 1
	 * @return cn.smile.common.utils.R
	 */
	@GetMapping("/ware/wareinfo/fare")
	public R getFare(@RequestParam("adrId") Long adrId);
	
	/**
	 * <p>库存锁定</p>
	 * @author smile
	 * @date 2021/3/3/003
	 * @param wareSkuLockVo 1
	 * @return cn.smile.common.utils.R
	 */
	@PostMapping("/ware/waresku/orderLockStock")
	public R orderLockStock(@RequestBody WareSkuLockVo wareSkuLockVo);
}
