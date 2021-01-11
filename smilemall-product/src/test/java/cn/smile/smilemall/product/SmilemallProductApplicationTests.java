package cn.smile.smilemall.product;

import cn.smile.smilemall.product.entity.BrandEntity;
import cn.smile.smilemall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmilemallProductApplicationTests {

	@Autowired
	private BrandService brandService;
	
	@Test
	void contextLoads() {
		BrandEntity id = brandService.getOne(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
		System.out.println(id);
		System.out.println("保存成功");
		
		
	}

}
