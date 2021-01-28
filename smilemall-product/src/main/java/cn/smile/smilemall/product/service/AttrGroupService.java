package cn.smile.smilemall.product.service;

import cn.smile.smilemall.product.entity.AttrEntity;
import cn.smile.smilemall.product.vo.AttrGroupRelationVo;
import cn.smile.smilemall.product.vo.AttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    PageUtils queryPage(Map<String,Object> params, Long catId);
    
    List<AttrEntity> queryAttrList(Long groupId);
    
    boolean deleteRelation(AttrGroupRelationVo[] vos);
    
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
	
	List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCateLogId(Long catelongId);
}

