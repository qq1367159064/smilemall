package cn.smile.smilemall.coupon.dao;

import cn.smile.smilemall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:29:06
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
