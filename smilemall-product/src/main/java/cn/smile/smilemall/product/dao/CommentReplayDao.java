package cn.smile.smilemall.product.dao;

import cn.smile.smilemall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
