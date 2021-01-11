package cn.smile.smilemall.member.controller;

import cn.smile.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/7
 */
@RestController
@RequestMapping("/member/test")
public class TestController {
	
	@GetMapping("/getTest")
	public R getTest() {
		return R.ok("我是会员服务");
	}
}
