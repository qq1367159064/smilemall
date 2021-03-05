package cn.smile.smilemall.product.dao;

import cn.smile.smilemall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:09
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
	
	/**
	 * <p>获取所有可以用来检索的属性id</p>
	 * @author Smile
	 * @date 2021/2/15/015
	 * @param attrIds 1
	 * @return java.util.List<java.lang.Long>
	 */
	List<Long> getSearchTypeAttrIds(@Param(value = "attrIds") List<Long> attrIds);
	
}
