package cn.smile.smilemall.product.service;

import cn.smile.smilemall.product.vo.AttrRespVo;
import cn.smile.smilemall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:09
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    boolean saveAttr(AttrVo attrVo);
    
    boolean updateAttr(AttrVo attrVo);
    
    PageUtils queryBaseAttrPage(Map<String, Object> parameter, Long cateId, String type);
    
    AttrRespVo getAttrInfo(Long attrid);
    
    /**
     * <p>获取所有可用来检索的属性id</p>
     * @author Smile
     * @date 2021/2/15/015
     * @param attrIds 1
     * @return java.util.List<java.lang.Long>
     */
    List<Long> getSearchTypeAttrIds(List<Long> attrIds);
}

