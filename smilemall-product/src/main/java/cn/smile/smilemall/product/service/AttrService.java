package cn.smile.smilemall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.product.entity.AttrEntity;

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
}
