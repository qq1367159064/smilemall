package cn.smile.common.constant;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/24/024
 */
public class ProductConstant {
	
	public enum  AttrEnum {
		ATTR_TYPE_BASE(1, "基本属性"),ATR_TYPE_SALE(0, "销售属性");
		
		AttrEnum(int code , String msg) {
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
		
		private int code;
		private String msg;
	}
}
