package cn.smile.smilemall.member.exception;

/**
 * <p>手机号已经存在</p>
 *
 * @author smile
 * @date 2021-02-23
 */
public class PhoneExistException extends Exception{
	public PhoneExistException() {
		super("手机号已经存在");
	}
}
