package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2018/6/28.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public class CallBackResult<T> {
//	public int BODY_NULL = 404;
//	public int RESULT_OK = 200;
//	public int SERVER_ERROR = 500;


	/**
	 * 成功与否的标识
	 */
	boolean result;
	/**
	 * 状态码
	 */
	int Code;
	String msg;
	/**
	 * 参数具体参数
	 */
	private T data;

	public CallBackResult() {
		setOK();
	}


	public CallBackResult(T data) {
		this.data = data;
		setOK();
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
		if (data == null) {
			this.setCode(400);
			this.setResult(false);
			this.setMsg("data is Null");
		}
	}

	private void setOK() {
		this.setCode(200);
		this.setResult(true);
		this.setMsg("OK");
	}
}
