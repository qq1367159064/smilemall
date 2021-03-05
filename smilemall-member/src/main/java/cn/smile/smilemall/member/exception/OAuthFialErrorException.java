package cn.smile.smilemall.member.exception;

/**
 * <p>社交登录失败</p>
 *
 * @author smile
 * @date 2021-02-23
 */
public class OAuthFialErrorException extends Exception{
	public OAuthFialErrorException() {
		super("社交登录失败");
	}
}
