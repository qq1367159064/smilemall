package cn.smile.smilemall.member.exception;

/**
 * <p>密码不正确</p>
 *
 * @author smile
 * @date 2021-02-23
 */
public class PasswordErrorException extends Exception{
	public PasswordErrorException() {
		super("密码不正确");
	}
}
