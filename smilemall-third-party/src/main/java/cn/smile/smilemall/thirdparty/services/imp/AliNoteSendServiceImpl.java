package cn.smile.smilemall.thirdparty.services.imp;

import cn.hutool.core.util.RandomUtil;
import cn.smile.common.exception.BizCodeEnume;
import cn.smile.common.smilemall.utils.SmileMallRedisTemplate;
import cn.smile.smilemall.thirdparty.services.AliNoteSendService;
import cn.smile.smilemall.thirdparty.vo.AliNoteVo;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-23
 */
@Service
public class AliNoteSendServiceImpl implements AliNoteSendService {

	private final static Long RETRY_TIMER = 60000L;
	private final CommonRequest request;
	private final IAcsClient client;
	
	public AliNoteSendServiceImpl(CommonRequest request, IAcsClient client) {
		this.request = request;
		this.client = client;
	}
	
	@Override
	public AliNoteVo sendNote(String phone) {
		AliNoteVo aliNoteVo = new AliNoteVo();
		try {
			request.putQueryParameter("PhoneNumbers", phone);
			String code = RandomUtil.randomString(RandomUtil.randomInt(4, 6));
			commonSendNote(phone, code, aliNoteVo);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return aliNoteVo;
	}
	
	@Override
	public AliNoteVo sendNote(String phone, String code) {
		AliNoteVo aliNoteVo = new AliNoteVo();
		try {
			request.putQueryParameter("PhoneNumbers", phone);
			commonSendNote(phone, code, aliNoteVo);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return aliNoteVo;
	}
	
	/**
	 * <p>公用发送代码块</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param phone 1
	 * @param code 2
	 * @param aliNoteVo 3
	 * @return void
	 */
	private void commonSendNote(String phone, String code, AliNoteVo aliNoteVo) throws ClientException {
		String exitCode = (String) SmileMallRedisTemplate.getValue(phone);
		if(!StringUtils.isEmpty(exitCode)) {
			String[] exitCodeList = exitCode.split("_");
			long timer = Long.parseLong(exitCodeList[1]);
			if(System.currentTimeMillis() - timer < RETRY_TIMER) {
				aliNoteVo.setNoteCode(exitCodeList[0]);
				aliNoteVo.setMsg(BizCodeEnume.VATLD_SMS_CODE.getMsg());
				aliNoteVo.setCode(BizCodeEnume.VATLD_SMS_CODE.getCode());
				return;
			}
		}
		request.putQueryParameter("TemplateParam", "{code: '" + code + "'}");
		CommonResponse response = client.getCommonResponse(request);
		aliNoteVo.setNoteCode(code);
		aliNoteVo.setCode(response.getHttpStatus());
		aliNoteVo.setMsg(response.getHttpResponse().getReasonPhrase());
		SmileMallRedisTemplate.setValue(phone, code + "_" + System.currentTimeMillis(), 600000L);
	}
}
