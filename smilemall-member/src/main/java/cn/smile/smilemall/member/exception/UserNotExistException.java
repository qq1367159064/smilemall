package cn.smile.smilemall.member.exception;

/**
 * <p>用户名不存在</p>
 *
 * @author smile
 * @date 2021-02-23
 */
public class UserNotExistException extends Exception{
	public UserNotExistException() {
		super("用户不存在");
	}
}
