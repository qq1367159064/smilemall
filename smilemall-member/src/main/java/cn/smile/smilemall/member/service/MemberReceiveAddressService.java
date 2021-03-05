package cn.smile.smilemall.member.service;

import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.member.entity.MemberReceiveAddressEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:32:47
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

	/**
	 * <p>通用分页查询</p>
	 * @author smile
	 * @date 2021/2/28/028
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 */
    PageUtils queryPage(Map<String, Object> params);
	
    /**
     * <p>获取用户地址</p>
     * @author smile
     * @date 2021/2/28/028
     * @param userId 1
     * @return java.util.List<cn.smile.smilemall.member.entity.MemberReceiveAddressEntity>
     */
	List<MemberReceiveAddressEntity> getAddress(Long userId);
}

