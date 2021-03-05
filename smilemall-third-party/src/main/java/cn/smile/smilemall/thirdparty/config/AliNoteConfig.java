package cn.smile.smilemall.thirdparty.config;

import cn.smile.smilemall.thirdparty.properties.AliNoteProperties;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-23
 */
@Configuration
@EnableConfigurationProperties(AliNoteProperties.class)
public class AliNoteConfig {

	@Bean
	public DefaultProfile defaultProfile(AliNoteProperties aliNoteProperties) {
		return DefaultProfile.getProfile("cn-hangzhou", aliNoteProperties.getAccessKeyId(),
				aliNoteProperties.getAccessSecret());
		
	}
	
	@Bean
	public IAcsClient iAcsClient(DefaultProfile defaultProfile) {
		return new DefaultAcsClient(defaultProfile);
	}
	
	@Bean
	public CommonRequest noteRequest(AliNoteProperties aliNoteProperties) {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("SignName",  aliNoteProperties.getSigName());
		request.putQueryParameter("TemplateCode", aliNoteProperties.getTemplateCode());
		return request;
	}
}
