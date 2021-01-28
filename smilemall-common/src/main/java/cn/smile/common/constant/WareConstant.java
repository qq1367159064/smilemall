package cn.smile.common.constant;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/28/028
 */
public class WareConstant {
	
	public enum PurchaseStatusEnum {
		CREATES(0, "新建"),
		ASSIGNED(1, "分配"),
		RECEIVE(2, "已领取"),
		FINISH(3, "已完成"),
		HASERROR(4, "有异常");
		
		int code;
		String msg;
		PurchaseStatusEnum(int code, String msg) {
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
	
	public enum PurchaseDetailEnum {
		CREATED(0, "新建"),
		ASSIDNED(1, "已分配"),
		BUYING(2, "正在采购"),
		FINISH(3, "已完成"),
		HASERROR(4, "有异常");
		int code;
		String msg;
		
		PurchaseDetailEnum(int code , String msg) {
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
}
