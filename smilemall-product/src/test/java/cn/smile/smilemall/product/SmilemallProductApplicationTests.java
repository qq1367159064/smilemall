package cn.smile.smilemall.product;

import cn.smile.smilemall.product.entity.BrandEntity;
import cn.smile.smilemall.product.service.BrandService;
import cn.smile.smilemall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
@Slf4j
class SmilemallProductApplicationTests {

	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	
	@Test
	void testFindCategoryLogPath() {
		Long[] categoryLogPath = categoryService.findCategoryLogPath(224L);
		Arrays.stream(categoryLogPath).forEach(System.out::print);
	}
	
	@Test
	void contextLoads() {
		BrandEntity id = brandService.getOne(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
		System.out.println(id);
		System.out.println("保存成功");
		
		
	}

}
