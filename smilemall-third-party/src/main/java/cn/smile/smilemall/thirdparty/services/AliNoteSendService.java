package cn.smile.smilemall.thirdparty.services;

import cn.smile.smilemall.thirdparty.vo.AliNoteVo;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-23
 */
public interface AliNoteSendService {
	
	/**
	 * <p>发送短信</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param phone 接收验证码的电话号码
	 * @return java.lang.Object
	 */
	AliNoteVo sendNote(String phone);
	
	/**
	 * <p>指定验证码发送</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param phone 1
	 * @param code 2
	 * @return cn.smile.smilemall.thirdparty.vo.AliNoteVo
	 */
	AliNoteVo sendNote(String phone, String code);
}
