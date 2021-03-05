package cn.smile.smilemall.thirdparty.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 短信信息配置类
 * @author Smile
 * @Documents
 * @creationTime 2020-10-2020/10/10
 */
@ConfigurationProperties(prefix = "smile.ali")
public class AliNoteProperties implements Serializable {
	private final static  long serialVersionUID = 1L;
	
	private String sigName;
	private String accessKeyId;
	private String accessSecret;
	private String templateCode;
	
	public String getSigName() {
		return sigName;
	}
	
	public void setSigName(String sigName) {
		this.sigName = sigName;
	}
	
	public String getAccessKeyId() {
		return accessKeyId;
	}
	
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	
	public String getAccessSecret() {
		return accessSecret;
	}
	
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	
	public String getTemplateCode() {
		return templateCode;
	}
	
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	@Override
	public String toString() {
		return "AgoodMotherAliProperties{" +
				"sigName='" + sigName + '\'' +
				", accessKeyId='" + accessKeyId + '\'' +
				", accessSecret='" + accessSecret + '\'' +
				", templateCode='" + templateCode + '\'' +
				'}';
	}
}
