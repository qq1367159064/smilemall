package cn.smile.smilemall.authserver.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-23
 */
@Data
public class UserRegisterVo {
	
	@NotEmpty(message = "用户必须提交")
	@Length(min = 6, max = 18, message = "用户名长度6~8")
	private String username;
	@NotEmpty(message = "密码不能为空")
	@Length(min = 6, max = 18, message = "密码长度6~8")
	private String password;
	@NotEmpty(message = "手机号不能为空")
	@Pattern(regexp = "^1[3-9][0-9]{9}$", message = "手机号格式不正确")
	private String phone;
	private String code;
}
