package cn.smile.smilemall.coupon.controller;

import cn.smile.common.utils.R;
import cn.smile.smilemall.coupon.feign.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/7
 */
@RestController
@RequestMapping("/coupon/test")
public class SmileTestController {
	
	@Autowired
	private TestFeign testFeign;
	
	@GetMapping("/getTest")
	public R getTest() {
		return R.ok(testFeign.getTest());
	}
	
}
