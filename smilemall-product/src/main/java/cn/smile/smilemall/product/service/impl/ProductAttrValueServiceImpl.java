package cn.smile.smilemall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;

import cn.smile.smilemall.product.dao.ProductAttrValueDao;
import cn.smile.smilemall.product.entity.ProductAttrValueEntity;
import cn.smile.smilemall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<ProductAttrValueEntity> page = this.page(
				new Query<ProductAttrValueEntity>().getPage(params),
				new QueryWrapper<ProductAttrValueEntity>()
		);
		
		return new PageUtils(page);
	}
	
	
	/**
	 * @param spuId 1
	 * @return java.util.List<cn.smile.smilemall.product.entity.ProductAttrValueEntity>
	 * @Description 查询spu的属性值
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	@Override
	public List<ProductAttrValueEntity> baseListForSpu(Long spuId) {
		return this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
	}
	
	/**
	 * @param spuId 1
	 * @param lists 2
	 * @return boolean
	 * @Description 更新属性
	 * @author Smile
	 * @date 2021/1/28/028
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean updateSpuAttr(Long spuId, List<ProductAttrValueEntity> lists) {
		this.baseMapper.deleteById(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
		List<ProductAttrValueEntity> collect = lists.stream().map(it -> {
			it.setSpuId(spuId);
			return it;
		}).collect(Collectors.toList());
		return this.saveBatch(collect);
	}
}