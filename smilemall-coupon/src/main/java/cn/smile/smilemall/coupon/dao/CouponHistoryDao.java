package cn.smile.smilemall.coupon.dao;

import cn.smile.smilemall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:29:06
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
