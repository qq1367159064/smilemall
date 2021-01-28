package cn.smile.smilemall.product.dao;

import cn.smile.smilemall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:09
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {
	boolean deleteBatchRelation(@Param("entitles")List<AttrAttrgroupRelationEntity> deleteByAttrId);
}
