package cn.smile.common.exception;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/14/014
 */
public enum BizCodeEnume {
	
	/**
	 * <p>
	 *     10: 通用
	 *        001:
	 *        002:
	 *     11: 商品
	 *     12: 订单
	 *     13: 购物车
	 *     14: 物流
	 *     21: 库存
	 * </p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @return
	 */
	VALID_EXCEPTION(10001, "参数格式校验失败"),
	UNKNOW_EXCEPTION(10000, "系统未知错误"),
	JSONE_PARSE_EXCEPTION(10002, "数据转换异常"),
	PRODUCT_UP_ERROR(11000, "商品上架异常"),
	VATLD_SMS_CODE(11002, "exit"),
	USER_EXIT_EXCEPTION(15001, "用户已经存在"),
	PHONE_EXIT_EXCEPTION(15002, "手机号已经存在"),
	ACCOUNT_ERROR(15003, "账号不存在"),
	PASSWORD_ERROR(15004, "密码不正确"),
	O_AUTH_FAIL_EXCEPTION(150005, "社交登录失败"),
	NOT_STOCK(21000, "没有库存");
	private int code ;
	private String msg;
	
	BizCodeEnume(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
