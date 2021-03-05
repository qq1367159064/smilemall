package cn.smile.smilemall.order.feign;

import cn.smile.common.annotation.SmileFeign;
import cn.smile.smilemall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-28
 */
@SmileFeign
@FeignClient(value = "cn-smile-smilemall-member")
public interface MemberFeignService {
	/**
	 * <p>获取用户地址</p>
	 * @author smile
	 * @date 2021/2/28/028
	 * @param userId 1
	 * @return java.util.List<cn.smile.smilemall.order.vo.MemberAddressVo>
	 */
	@GetMapping("/member/memberreceiveaddress/getAddress/{userId}")
	public List<MemberAddressVo> getAddressByUserId(@PathVariable(value = "userId") Long userId);
}
