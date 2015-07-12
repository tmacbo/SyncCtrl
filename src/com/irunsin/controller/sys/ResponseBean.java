package com.irunsin.controller.sys;

import java.io.Serializable;

public class ResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public ResponseBean(String requestId, String respCode) {
		super();
		this.respCode = respCode;
		this.requestKey = requestId;
	}

	private String respCode;

	private String requestKey;

	private String respMsg;

	private String respScrMsg;

	public boolean isOk() {
		if (respCode != null && respCode.equals(RESP_CODE_OK)) {
			return true;
		}
		return false;
	}

	public static final String RESP_CODE_OK = "0";
	public static final String RESP_CODE_NET_ERROR = "NET_ERROR";
	public static final String RESP_READ_ERROR = "READ_ERROR";
	public static final String RESP_CODE_PARSER_ERROR = "PARSER_ERROR";

	public static boolean isTimeOut(String code) {
		if (code != null && code.equals("9999")) {
			return true;
		}
		return false;
	}

	public ResponseBean() {
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getRespScrMsg() {
		return respScrMsg;
	}

	public void setRespScrMsg(String respScrMsg) {
		this.respScrMsg = respScrMsg;
	}

	public String getRequestKey() {
		return requestKey;
	}

	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}

}
