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

import cn.smile.smilemall.product.dao.SpuImagesDao;
import cn.smile.smilemall.product.entity.SpuImagesEntity;
import cn.smile.smilemall.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SpuImagesEntity> page = this.page(
				new Query<SpuImagesEntity>().getPage(params),
				new QueryWrapper<SpuImagesEntity>()
		);
		
		return new PageUtils(page);
	}
	
	/**
	 * @param id     1
	 * @param images 2
	 * @return void
	 * @Description 批量保存spu的图片信息
	 * @author Smile
	 * @date 2021/1/24/024
	 */
	@Override
	public void saveSpuImages(Long id, List<String> images) {
		if(images != null && images.size() != 0) {
			List<SpuImagesEntity> collect = images.stream().map(item -> {
				SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
				spuImagesEntity.setSpuId(id);
				spuImagesEntity.setImgUrl(item);
				return spuImagesEntity;
			}).collect(Collectors.toList());
			this.saveBatch(collect);
		}
	}
}