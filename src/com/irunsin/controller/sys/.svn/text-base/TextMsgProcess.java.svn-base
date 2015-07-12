package com.irunsin.controller.sys;

import java.util.HashMap;

import android.content.Context;

public class TextMsgProcess implements Runnable {
	private Context context;
	private String url;
	private String request;
	private String response;

	private ResponseBean respBean;
	private TextMsgParser parser;
	private String processId;
	private ProcessListener processListener;
	private boolean isCanceled = false;

	private String encoding;
	private NetworkManager manager;

	public static boolean isTimeOut = false;

	public TextMsgProcess(Context context, String processId, String url,
			String request, TextMsgParser parser, String encoding,
			ProcessListener processListener) {
		super();
		this.context = context;
		this.url = url;
		this.request = request;
		this.parser = parser;
		this.processId = processId;
		this.processListener = processListener;
		this.encoding = encoding;
	}

	public void cancel() {
		if (isCanceled) {
			return;
		}
		isCanceled = true;
		if (manager != null) {
			manager.cancel();
		}
	}

	public void run() {
		manager = new NetworkManager(context);
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("Content-type", "application/x-www-form-urlencoded;charset="
				+ encoding);
		response = manager.SendAndWaitResponse(request, url, encoding, m,
				Address.isLog(processId));

		// 处理器为空时，无法处理消息
		if (processListener == null) {
			Global.error("Response's processListener is null, url=["
					+ Address.getUrl(processId) + "]");
			return;
		}
		// 如果获取的报文为空
		if (response == null) {
			if (!isCanceled) {
				processListener.onDone(new ResponseBean(processId,
						ResponseBean.RESP_CODE_NET_ERROR));
				Global.error("Response string is null, url=["
						+ Address.getUrl(processId) + "]");
			}
			return;
		}

		// 解析报文
		if (parser == null) {
			if (!isCanceled) {
				processListener.onDone(new ResponseBean(processId,
						ResponseBean.RESP_CODE_PARSER_ERROR));
				Global.error("Response parser is null, url=["
						+ Address.getUrl(processId) + "]");
			}
			return;
		} else {

			if (!isCanceled) {
				respBean = parser.parser(response);
			}
		}

		// 如果解析结果为空
		if (respBean == null) {

			if (!isCanceled) {
				try {
					processListener.onDone(new ResponseBean(processId,
							ResponseBean.RESP_CODE_PARSER_ERROR));
				} catch (Exception e) {
					e.printStackTrace();
				}
				Global.error("Response parser result is null, url=["
						+ Address.getUrl(processId) + "]");
			}
			return;
		} else
		// 解析结果不为空时，处理该请求
		{
			if (!isCanceled) {

				respBean.setRequestKey(processId);
				processListener.onDone(respBean);
			}
		}
	}

}
