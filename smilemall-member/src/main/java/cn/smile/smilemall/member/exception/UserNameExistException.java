package cn.smile.smilemall.member.exception;

/**
 * <p>用户名称存在</p>
 *
 * @author smile
 * @date 2021-02-23
 */
public class UserNameExistException  extends RuntimeException{
	
	public UserNameExistException() {
		super("用户名已经存在");
	}
}
