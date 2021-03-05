package cn.smile.smilemall.authserver.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>社交登录用户信息</p>
 *
 * @author smile
 * @date 2021-02-23
 */
@Data
public class SocialUserVo implements Serializable {
	public final static Long serialVersionUID = 1L;
	private String access_token;
	private Long expires_in;
	private String remind_in;
	private String uid;
}
