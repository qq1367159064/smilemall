package cn.smile.smilemall.member.dao;

import cn.smile.smilemall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:32:47
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
