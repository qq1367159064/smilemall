package cn.smile.smilemall.thirdparty.controller;

import cn.smile.common.utils.R;
import cn.smile.smilemall.thirdparty.services.AliNoteSendService;
import cn.smile.smilemall.thirdparty.vo.AliNoteVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>短信验证码</p>
 *
 * @author smile
 * @date 2021-02-23
 */
@RestController
@RequestMapping("/sms")
public class NoteSendController {
	
	private final AliNoteSendService aliNoteSendService;
	
	public NoteSendController(AliNoteSendService aliNoteSendService) {
		this.aliNoteSendService = aliNoteSendService;
	}
	
	/**
	 * <p>发送验证码</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param phone 1
	 * @return cn.smile.common.utils.R
	 */
	@GetMapping("/sendCodeAutoCreateCode")
	public R sendNote(@RequestParam(value = "phone") String phone) {
		AliNoteVo aliNoteVo = aliNoteSendService.sendNote(phone);
		return R.ok().put("note", aliNoteVo);
	}
	
	/**
	 * <p>自定义code</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param phone 1
	 * @param code 2
	 * @return cn.smile.common.utils.R
	 */
	@GetMapping("/sendCodeCustomerCode")
	public R sendNote(@RequestParam(value = "phone") String phone, @RequestParam(value = "code")String code) {
		AliNoteVo aliNoteVo = aliNoteSendService.sendNote(phone, code);
		return R.ok().put("note", aliNoteVo);
	}
}
