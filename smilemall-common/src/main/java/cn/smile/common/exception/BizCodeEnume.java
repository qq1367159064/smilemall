package cn.smile.common.exception;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/14/014
 */
public enum BizCodeEnume {
	
	VALID_EXCEPTION(10001, "参数格式校验失败"),
	UNKNOW_EXCEPTION(10000, "系统未知错误"),
	JSONE_PARSE_EXCEPTION(10002, "数据转换异常");
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
