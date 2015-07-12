package com.irunsin.controller.sys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import android.content.Context;

public class NetworkManager {

	private int connectTimeout = 30 * 1000;
	private int readTimeout = 30 * 1000;
	Proxy mProxy = null;
	Context mContext;
	public String encoding = "UTF-8";

	private boolean isCanceled = false;
	HttpURLConnection httpURLConnection;

	public void cancel() {

		isCanceled = true;
		try {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NetworkManager(Context context, String encoding) {
		this.mContext = context;
		this.encoding = encoding;
		setDefaultHostnameVerifier();
	}

	public NetworkManager(Context context) {
		this.mContext = context;
		setDefaultHostnameVerifier();
	}

	public NetworkManager(Context context, DownloadListener downloadListener) {
		this.mContext = context;
		this.downloadListener = downloadListener;
	}

	/**
	 * 设置代理
	 */
	public void detectProxy() {
		// ConnectivityManager cm = (ConnectivityManager)
		// mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo ni = cm.getActiveNetworkInfo();
		// if (ni != null && ni.isAvailable() && ni.getType() ==
		// ConnectivityManager.TYPE_WIFI) {
		//
		// String proxyHost = android.net.Proxy.getDefaultHost();
		// int port = android.net.Proxy.getDefaultPort();
		// if (proxyHost != null) {
		// final InetSocketAddress sa = new InetSocketAddress(proxyHost, port);
		// mProxy = new Proxy(Proxy.Type.HTTP, sa);
		// }
		// }
	}

	private void setDefaultHostnameVerifier() {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	public byte[] sendAndGetByteArray(String strUrl) {
		if (strUrl == null || isCanceled) {
			return null;
		}
		detectProxy();

		try {
			URL url = new URL(strUrl);

			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url
						.openConnection(mProxy);
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setDoInput(true);

			if (!isCanceled) {
				httpURLConnection.connect();
			}

			InputStream content = httpURLConnection.getInputStream();
			try {
				return StreamsUtils.readByteArrayFromStream(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cancel();
		}

		return null;
	}

	public String SendAndWaitResponse(String strReqData, String strUrl,
			String _encoding) {
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("Content-type", "application/x-www-form-urlencoded;charset="
				+ encoding);
		return SendAndWaitResponse(strReqData, strUrl, _encoding, m, false);
	}

	public String SendAndWaitResponse(String strReqData, String strUrl,
			String _encoding, Map<String, String> httpHeads, boolean isLog) {

		if (_encoding == null) {
			_encoding = this.encoding;
		}
		if (isLog) {
			Global.debug("\r\nReqUrl:" + strUrl);
			Global.debug("\r\nReqData:" + strReqData);
		}
		detectProxy();

		String strResponse = null;

		try {
			URL url = new URL(strUrl);

			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url
						.openConnection(mProxy);
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setDoInput(true);
			if (httpHeads != null) {
				for (String key : httpHeads.keySet()) {
					httpURLConnection.addRequestProperty(key,
							httpHeads.get(key));
				}
			}

			if (!isCanceled && strReqData != null) {
				httpURLConnection.setDoOutput(true);
				OutputStream os = httpURLConnection.getOutputStream();
				os.write(strReqData.getBytes(_encoding));
				os.flush();
			}
			if (!isCanceled) {
				InputStream content = httpURLConnection.getInputStream();
				strResponse = new String(
						StreamsUtils.readByteArrayFromStream(content),
						_encoding);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cancel();
		}

		if (isLog) {
			Global.debug("Resp:" + strResponse + "\r\n");
		}
		return strResponse;
	}

	public boolean urlDownloadToFile(Context context, String strurl, String path) {
		boolean bRet = false;

		detectProxy();

		try {
			URL url = new URL(strurl);
			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url
						.openConnection(mProxy);
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setDoInput(true);
			if (!isCanceled) {

				File dir = new File(Global.PLUGIN_CACHE_NAME);
				File file = new File(path);
				if (!dir.exists()) {// 判断文件夹目录是否存在
					dir.mkdir();// 如果不存在则创建
				}
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);

				byte[] temp = new byte[1024];
				int i = 0;

				InputStream is = httpURLConnection.getInputStream();
				while ((i = is.read(temp)) > 0) {
					if (isCanceled) {
						break;
					}
					fos.write(temp, 0, i);
				}

				fos.close();
				is.close();

				bRet = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cancel();
		}

		return bRet;
	}

	public NetworkManager downloadFiles(String urlStr) {

		detectProxy();

		try {
			URL url = new URL(urlStr);
			if (mProxy != null) {
				httpURLConnection = (HttpURLConnection) url
						.openConnection(mProxy);
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			if (downloadListener != null && !isCanceled) {
				downloadListener.setMaxDownloadCount(httpURLConnection
						.getContentLength());
			}

			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);

			InputStream is = httpURLConnection.getInputStream();

			if (downloadListener != null && !isCanceled) {
				downloadListener.downloadStarted();
			}

			byte[] temp = new byte[1024 * 8];
			int i = 0;
			while ((i = is.read(temp)) > 0 && !isCanceled) {

				if (downloadListener != null && !isCanceled) {
					downloadListener.download(temp, 0, i);
				}
			}

			if (downloadListener != null && !isCanceled) {
				downloadListener.downloadfinished();
			}
			is.close();
			httpURLConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();

			if (downloadListener != null && !isCanceled) {
				downloadListener.downloadError(e);
			}
		} finally {
			cancel();
		}

		return this;
	}

	DownloadListener downloadListener;

	public static class DownloadAdapter implements DownloadListener {
		public void download(int ch) {
		}

		public void download(byte[] buffer, int start, int length) {
		}

		public void downloadError(Exception e) {
		}

		public void downloadStarted() {
		}

		public void downloadfinished() {
		}

		public void setMaxDownloadCount(int maxCount) {
		}
	}

	public static interface DownloadListener {
		/**
		 * 下载到一个字节
		 * 
		 * @param ch
		 */
		public void download(int ch);

		/**
		 * 下载到一个字节数组
		 * 
		 * @param buffer
		 * @param start
		 * @param length
		 */
		public void download(byte[] buffer, int start, int length);

		/**
		 * 设置本次下载的字节数
		 * 
		 * @param maxCount
		 */
		public void setMaxDownloadCount(int maxCount);

		/**
		 * 通知现在完成
		 */
		public void downloadfinished();

		/**
		 * 下载开始
		 */
		public void downloadStarted();

		/**
		 * 下载发送错误
		 */
		public void downloadError(Exception e);
	}

	public static InputStream getStreamFromURL(String imageURL) {
		InputStream in = null;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			in = connection.getInputStream();

		} catch (Exception e) {
			// TODOAuto-generatedcatchblock
			e.printStackTrace();
		}
		return in;

	}
}
