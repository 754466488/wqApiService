package com.dzw.micro.wq.application.httpclient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dzw.micro.wq.application.domain.constant.ErrorCode;
import com.dzw.micro.wq.application.domain.constant.SymbolConstant;
import com.dzw.micro.wq.application.domain.exception.HttpCallException;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.json.FastJson;
import com.dzw.micro.wq.application.json.JsonException;
import com.dzw.micro.wq.application.utils.BeanUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于HttpCliet-4.5.3封装对远程HTTP接口的访问<br>
 *
 * @author lyb
 * @suggest 如果项目中的客户端连接的目标主机只有一个<br>
 * 配置一个目标主机对应一个HttpClientUtils实例是最高效稳定的做法(maxPerRoute和maxTotal可配置相同)<br>
 * HttpClient有默认的失败重试策略,最大3次
 * <p>
 * 2022-10-13
 * @suggest 注意使用https协议无法访问没有证书的网址，会在建立链接的时候类似如下报错：
 * Caused by: java.security.cert.CertificateExpiredException: NotAfter: Thu Sep 01 07:59:59 CST 2022
 * <p>
 * 2022-10-13
 * 解决方式：新增一个构造方法，用于构造跳过证书验证阶段的HttpClientUtils实例
 * @thread-safe
 */
@Slf4j
public class HttpClientUtils {
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_USER_AGENT = "Apache-HttpClient-4.5.3";
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	/**
	 * 默认最大请求超时时间10秒,指客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
	 */
	public static final int DEFAULT_REQUEST_TIMEOUT = 10000;
	/**
	 * 默认最大请求超时时间10秒, http clilent中从connetcion pool中获得一个connection的超时时间
	 */
	public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 3000;
	/**
	 * 默认指客户端和服务器建立连接的timeout
	 */
	public static final int DEFAULT_CONNECT_TIMEOUT = 3000;
	/**
	 * 默认keepalive时间1分钟
	 */
	public static final long DEFAULT_KEEPALIVE_TIME = 60000L;

	/**
	 * http连接池管理器
	 */
	private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
	/**
	 * http客户端
	 */
	private CloseableHttpClient closeableHttpClient;

	/**
	 * 默认客户端级别的设置
	 */
	private RequestConfig defaultRequestConfig;

	/**
	 * 默认链接相关的设置
	 */
	private ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom()
			.setBufferSize(this.getSocketBufferSize())
			.setCharset(Charset.forName(DEFAULT_ENCODING))
			.build();
	/**
	 * 默认Socket相关的设置
	 */
	private SocketConfig defaultSocketConfig;

	/**
	 * 定期回收过期链接线程
	 */
	private static final IdleConnectionMonitorThread idleThread;

	static {
		idleThread = new IdleConnectionMonitorThread();
		idleThread.setDaemon(true);
		idleThread.start();
	}

	public HttpClientUtils() {
		this(DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_REQUEST_TIMEOUT, DEFAULT_USER_AGENT);
	}

	public HttpClientUtils(boolean trustAllServerCert) {
		this(DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_REQUEST_TIMEOUT, DEFAULT_USER_AGENT, trustAllServerCert);
	}

	public HttpClientUtils(String userAgent) {
		this(DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_REQUEST_TIMEOUT, userAgent);
	}

	public HttpClientUtils(int socketTimeout, String userAgent) {
		this(DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, socketTimeout, userAgent);
	}

	public HttpClientUtils(int socketTimeout) {
		this(DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, socketTimeout, DEFAULT_USER_AGENT);
	}

	public HttpClientUtils(int socketTimeout, boolean trustAllServerCert) {
		this(DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, socketTimeout, DEFAULT_USER_AGENT, trustAllServerCert);
	}

	public HttpClientUtils(int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
		this(connectionRequestTimeout, connectTimeout, socketTimeout, DEFAULT_USER_AGENT);
	}

	public HttpClientUtils(int connectionRequestTimeout, int connectTimeout, int socketTimeout, boolean trustAllServerCert) {
		this(connectionRequestTimeout, connectTimeout, socketTimeout, DEFAULT_USER_AGENT, trustAllServerCert);
	}

	private HttpClientUtils(int connectionRequestTimeout, int connectTimeout, int socketTimeout, String userAgent) {
		this(connectionRequestTimeout, connectTimeout, socketTimeout, userAgent, false);
	}

	private HttpClientUtils(int connectionRequestTimeout, int connectTimeout, int socketTimeout, String userAgent, boolean trustAllServerCert) {
		defaultRequestConfig = RequestConfig.custom()
				.setRedirectsEnabled(true)  //自动处理重定向
				.setMaxRedirects(10)        //自动重定向次数
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectTimeout)
				.setSocketTimeout(socketTimeout)
				.build();
		defaultSocketConfig = SocketConfig.custom()
				.setTcpNoDelay(this.isTcpNodelay())
				.setSoTimeout(socketTimeout)
				.setSoLinger(this.getSoLinger())
				.setSoReuseAddress(this.isSoReuseaddr())
				.setSoKeepAlive(this.isKeepAlive())
				.build();
		/**
		 * 创建链接管理器
		 */
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", createSSLConnectionSocketFactory(trustAllServerCert))
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.build();
		poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
		poolingHttpClientConnectionManager.setDefaultConnectionConfig(defaultConnectionConfig);
		poolingHttpClientConnectionManager.setDefaultSocketConfig(defaultSocketConfig);

		/**
		 * 创建httpClient
		 */
		closeableHttpClient = HttpClients.custom()
				.setRetryHandler(new HttpRequestRetryHandler())                 //设置请求重试Handler
				.setKeepAliveStrategy(new HttpConnectionKeepAliveStrategy())    //设置keep-alive连接保持活动的策略
				.setUserAgent(userAgent)
				.setConnectionManagerShared(true)
				.setDefaultRequestConfig(defaultRequestConfig)
				.setConnectionManager(poolingHttpClientConnectionManager)
				.build();
		/**
		 * 定期回收连接池中失效链接线程
		 */
		idleThread.addConnMgr(poolingHttpClientConnectionManager);
	}

	private SSLConnectionSocketFactory createSSLConnectionSocketFactory(boolean trustAllServerCert) {
		SSLConnectionSocketFactory sslsf;
		if (trustAllServerCert) {
			try {
				SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
				// Trust all certificaties
				sslContextBuilder.loadTrustMaterial(null, (x509Certificates, s) -> true);
				sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE);
			} catch (Exception e) {
				throw new RuntimeException("创建信任服务端证书模式的HttpClientUtils实例失败", e);
			}
		} else {
			sslsf = SSLConnectionSocketFactory.getSocketFactory();
		}
		return sslsf;
	}

	public void shutdown() {
		idleThread.shutdown();
		org.apache.http.client.utils.HttpClientUtils.closeQuietly(closeableHttpClient);
	}

	public String httpCallGet(String url) {
		return httpCallGet(url, null, null, 0, DEFAULT_ENCODING);
	}

	public String httpCallGet(String url, Map<String, String> queryParams) {
		return httpCallGet(url, queryParams, null, 0, DEFAULT_ENCODING);
	}

	public String httpCallGet(String url, Map<String, String> queryParams, int requestTimeout) {
		return httpCallGet(url, queryParams, null, requestTimeout, DEFAULT_ENCODING);
	}

	public String httpCallGet(String url, Map<String, String> queryParams, Map<String, String> headerMap) {
		return httpCallGet(url, queryParams, headerMap, 0, DEFAULT_ENCODING);
	}

	public String httpCallGet(String url, Map<String, String> queryParams, Map<String, String> headerMap, int requestTimeout) {
		return httpCallGet(url, queryParams, headerMap, requestTimeout, DEFAULT_ENCODING);
	}

	public String httpCallGet(String url, Map<String, String> queryParams, int requestTimeout, String encoding) {
		return httpCallGet(url, queryParams, null, requestTimeout, encoding);
	}

	public <T> T httpCallGet(String url, Class<T> respType) {
		return httpCallGet(url, null, null, 0, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallGet(String url, Object queryParams, Class<T> respType) {
		return httpCallGet(url, queryParams, null, 0, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallGet(String url, Object queryParams, int requestTimeout, Class<T> respType) {
		return httpCallGet(url, queryParams, null, requestTimeout, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallGet(String url, Object queryParams, Map<String, String> headerMap, Class<T> respType) {
		return httpCallGet(url, queryParams, headerMap, 0, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallGet(String url, Object queryParams, Map<String, String> headerMap, int requestTimeout, Class<T> respType) {
		return httpCallGet(url, queryParams, headerMap, requestTimeout, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallGet(String url, Object queryParams, int requestTimeout, String encoding, Class<T> respType) {
		return httpCallGet(url, queryParams, null, requestTimeout, encoding, respType);
	}

	public <T> T httpCallGet(String url, Object queryParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respType) {
		String result = httpCallGet(url, objectParam2MapParam(queryParams), headerMap, requestTimeout, encoding);
		return Strings.isNullOrEmpty(result) ? null : FastJson.jsonStr2Object(result, respType);
	}

	public <T> Resp<T> httpCallGetRtnResp(String url, Class<T> respDataType) {
		return httpCallGetRtnResp(url, null, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallGetRtnResp(String url, Object queryParams, Class<T> respDataType) {
		return httpCallGetRtnResp(url, queryParams, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallGetRtnResp(String url, Object queryParams, int requestTimeout, Class<T> respDataType) {
		return httpCallGetRtnResp(url, queryParams, null, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallGetRtnResp(String url, Object queryParams, Map<String, String> headerMap, Class<T> respDataType) {
		return httpCallGetRtnResp(url, queryParams, headerMap, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallGetRtnResp(String url, Object queryParams, Map<String, String> headerMap, int requestTimeout, Class<T> respDataType) {
		return httpCallGetRtnResp(url, queryParams, headerMap, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallGetRtnResp(String url, Object queryParams, int requestTimeout, String encoding, Class<T> respDataType) {
		return httpCallGetRtnResp(url, queryParams, null, requestTimeout, encoding, respDataType);
	}

	public <T> Resp<T> httpCallGetRtnResp(String url, Object queryParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respDataType) {
		String result = httpCallGet(url, objectParam2MapParam(queryParams), headerMap, requestTimeout, encoding);
		return json2DataResp(result, respDataType);
	}

	public <T> Resp<List<T>> httpCallGetRtnListDataResp(String url, Class<T> respDataType) {
		return httpCallGetRtnListDataResp(url, null, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallGetRtnListDataResp(String url, Object queryParams, Class<T> respDataType) {
		return httpCallGetRtnListDataResp(url, queryParams, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallGetRtnListDataResp(String url, Object queryParams, int requestTimeout, Class<T> respDataType) {
		return httpCallGetRtnListDataResp(url, queryParams, null, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallGetRtnListDataResp(String url, Object queryParams, Map<String, String> headerMap, Class<T> respDataType) {
		return httpCallGetRtnListDataResp(url, queryParams, headerMap, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallGetRtnListDataResp(String url, Object queryParams, Map<String, String> headerMap, int requestTimeout, Class<T> respDataType) {
		return httpCallGetRtnListDataResp(url, queryParams, headerMap, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallGetRtnListDataResp(String url, Object queryParams, int requestTimeout, String encoding, Class<T> respDataType) {
		return httpCallGetRtnListDataResp(url, queryParams, null, requestTimeout, encoding, respDataType);
	}

	public <T> Resp<List<T>> httpCallGetRtnListDataResp(String url, Object queryParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respDataType) {
		String result = httpCallGet(url, objectParam2MapParam(queryParams), headerMap, requestTimeout, encoding);
		return json2ListDataResp(result, respDataType);
	}

	/**
	 * get请求文本
	 *
	 * @param url            - 请求URL
	 * @param queryParams    - 查询参数,如果要保证参数顺序请使用LinkedHashMap
	 * @param headerMap      - 请求头
	 * @param requestTimeout - 请求超时时间,毫秒数
	 * @param encoding       字符编码
	 * @return string
	 * @throws IllegalArgumentException
	 * @throws HttpCallException
	 */
	public String httpCallGet(String url, Map<String, String> queryParams, Map<String, String> headerMap, int requestTimeout, String encoding) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "Param url must be not null and empty");
		Preconditions.checkArgument(url.startsWith("http"), "Param url must be startWith 'http'");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

		String urlToSend = url;
		/**
		 * 创建查询参数,如果设置了queryParams
		 */
		if (MapUtils.isNotEmpty(queryParams)) {
			List<NameValuePair> qparams = Lists.newArrayList();
			for (Entry<String, String> entry : queryParams.entrySet()) {
				qparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			String queryParamStr = URLEncodedUtils.format(qparams, encoding);
			if (url.endsWith(SymbolConstant.QUESTION)) {
				urlToSend = urlToSend + queryParamStr;
			} else {
				urlToSend = urlToSend + SymbolConstant.QUESTION + queryParamStr;
			}
		}

		HttpGet httpGet = new HttpGet(urlToSend);
		/**
		 * 设置超时，覆盖httpClient默认参数
		 */
		if (requestTimeout > 0) {
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(requestTimeout)
					.build();
			httpGet.setConfig(requestConfig);
		}
		/**
		 * 设置请求头
		 */
		if (MapUtils.isNotEmpty(headerMap)) {
			for (Entry<String, String> header : headerMap.entrySet()) {
				httpGet.addHeader(header.getKey(), header.getValue());
			}
		}
		return this.httpCallInternal("httpCallGet", httpGet, url, queryParams, headerMap, requestTimeout, encoding);
	}

	private String httpCallInternal(String callMethodName, HttpUriRequest httpUriRequest, String url, Object param, Map<String, String> headerMap, int requestTimeout, String encoding) {
		String result;
		long start = System.currentTimeMillis();
		StringBuilder logSb = new StringBuilder(callMethodName).append("|");
		logSb.append("Url:").append(url).append("|");
		logSb.append("Timeout:").append(requestTimeout).append("|");
		logSb.append("Header:").append(print(headerMap)).append("|");
		logSb.append("Param:").append(print(param)).append("|");

		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = closeableHttpClient.execute(httpUriRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity(), encoding);
			} else {
				result = FastJson.object2JsonStr(Resp.error(ErrorCode.STATUS_CODE_NOT_SC_OK, httpResponse.getStatusLine().toString()));
			}

			logSb.append("Result:").append(result).append("|");
		} catch (IOException ex) {
			StringBuilder errorMsgSB = new StringBuilder();
			errorMsgSB.append("[").append(url).append("]");

			Throwable rootCause = Throwables.getRootCause(ex);
			if (rootCause instanceof SocketTimeoutException && StringUtils.containsIgnoreCase(rootCause.getMessage(), "connect timed out")) {
				errorMsgSB.append("[").append(rootCause.getMessage()).append("][").append(defaultRequestConfig.getConnectTimeout()).append("毫秒]");
			} else if (rootCause instanceof SocketTimeoutException && StringUtils.containsIgnoreCase(rootCause.getMessage(), "read timed out")) {
				errorMsgSB.append("[").append(rootCause.getMessage()).append("][").append(requestTimeout > 0 ? requestTimeout : defaultRequestConfig.getSocketTimeout()).append("毫秒]");
			} else if (rootCause instanceof ConnectionPoolTimeoutException) {
				errorMsgSB.append("[").append(rootCause.getMessage()).append("][").append(defaultRequestConfig.getConnectionRequestTimeout()).append("毫秒]");
			} else {
				errorMsgSB.append("[").append(ex.getMessage()).append("]");
			}

			logSb.append("Error:").append(errorMsgSB.toString()).append("|");
			throw new HttpCallException(errorMsgSB.toString(), ex);
		} finally {
			long end = System.currentTimeMillis();
			logSb.append("TimeCost:").append(end - start);
			if (log.isDebugEnabled()) {
				log.debug(logSb.toString());
			}

			org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpResponse);
		}
		return result;
	}

	public String httpCallPostForm(String url, Map<String, String> postParams) {
		return httpCallPostForm(url, postParams, null, 0, DEFAULT_ENCODING);
	}

	public String httpCallPostForm(String url, Map<String, String> postParams, int requestTimeout) {
		return httpCallPostForm(url, postParams, null, requestTimeout, DEFAULT_ENCODING);
	}

	public String httpCallPostForm(String url, Map<String, String> postParams, int requestTimeout, String encoding) {
		return httpCallPostForm(url, postParams, null, requestTimeout, encoding);
	}

	public String httpCallPostForm(String url, Map<String, String> postParams, Map<String, String> headerMap) {
		return httpCallPostForm(url, postParams, headerMap, 0, DEFAULT_ENCODING);
	}

	public String httpCallPostForm(String url, Map<String, String> postParams, Map<String, String> headerMap, int requestTimeout) {
		return httpCallPostForm(url, postParams, headerMap, requestTimeout, DEFAULT_ENCODING);
	}

	public <T> T httpCallPostForm(String url, Object postParams, Class<T> respType) {
		return httpCallPostForm(url, postParams, null, 0, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostForm(String url, Object postParams, int requestTimeout, Class<T> respType) {
		return httpCallPostForm(url, postParams, null, requestTimeout, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostForm(String url, Object postParams, Map<String, String> headerMap, Class<T> respType) {
		return httpCallPostForm(url, postParams, headerMap, 0, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostForm(String url, Object postParams, Map<String, String> headerMap, int requestTimeout, Class<T> respType) {
		return httpCallPostForm(url, postParams, headerMap, requestTimeout, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostForm(String url, Object postParams, int requestTimeout, String encoding, Class<T> respType) {
		return httpCallPostForm(url, postParams, null, requestTimeout, encoding, respType);
	}

	public <T> T httpCallPostForm(String url, Object postParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respType) {
		String result = httpCallPostForm(url, objectParam2MapParam(postParams), headerMap, requestTimeout, encoding);
		return Strings.isNullOrEmpty(result) ? null : FastJson.jsonStr2Object(result, respType);
	}

	public <T> Resp<T> httpCallPostFormRtnResp(String url, Object postParams, Class<T> respDataType) {
		return httpCallPostFormRtnResp(url, postParams, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostFormRtnResp(String url, Object postParams, int requestTimeout, Class<T> respDataType) {
		return httpCallPostFormRtnResp(url, postParams, null, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostFormRtnResp(String url, Object postParams, Map<String, String> headerMap, Class<T> respDataType) {
		return httpCallPostFormRtnResp(url, postParams, headerMap, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostFormRtnResp(String url, Object postParams, Map<String, String> headerMap, int requestTimeout, Class<T> respDataType) {
		return httpCallPostFormRtnResp(url, postParams, headerMap, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostFormRtnResp(String url, Object postParams, int requestTimeout, String encoding, Class<T> respDataType) {
		return httpCallPostFormRtnResp(url, postParams, null, requestTimeout, encoding, respDataType);
	}

	public <T> Resp<T> httpCallPostFormRtnResp(String url, Object postParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respDataType) {
		String result = httpCallPostForm(url, objectParam2MapParam(postParams), headerMap, requestTimeout, encoding);
		return json2DataResp(result, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostFormRtnListDataResp(String url, Object postParams, Class<T> respDataType) {
		return httpCallPostFormRtnListDataResp(url, postParams, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostFormRtnListDataResp(String url, Object postParams, int requestTimeout, Class<T> respDataType) {
		return httpCallPostFormRtnListDataResp(url, postParams, null, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostFormRtnListDataResp(String url, Object postParams, Map<String, String> headerMap, Class<T> respDataType) {
		return httpCallPostFormRtnListDataResp(url, postParams, headerMap, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostFormRtnListDataResp(String url, Object postParams, Map<String, String> headerMap, int requestTimeout, Class<T> respDataType) {
		return httpCallPostFormRtnListDataResp(url, postParams, headerMap, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostFormRtnListDataResp(String url, Object postParams, int requestTimeout, String encoding, Class<T> respDataType) {
		return httpCallPostFormRtnListDataResp(url, postParams, null, requestTimeout, encoding, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostFormRtnListDataResp(String url, Object postParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respDataType) {
		String result = httpCallPostForm(url, objectParam2MapParam(postParams), headerMap, requestTimeout, encoding);
		return json2ListDataResp(result, respDataType);
	}

	private <T> Resp<List<T>> json2ListDataResp(String result, Class<T> respDataType) {
		if (StringUtils.isBlank(result)) {
			return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, "返回值为空串");
		}

		try {
			JSONObject resultJsonObj = FastJson.jsonStr2Object(result, JSONObject.class);
			String code = resultJsonObj.getString("code");
			String message = resultJsonObj.getString("message");
			Object data = resultJsonObj.get("data");

			if (StringUtils.isBlank(code)) {
				return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, result);
			}
			if (data != null && data instanceof JSONArray) {
				data = (((JSONArray) data).toJavaList(respDataType));
			}
			if (data != null && data instanceof JSONObject) {
				return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, "data字段不是[]类型");
			}
			return new Resp(code, message, data);
		} catch (JsonException e) {
			return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, "非json对象格式");
		}
	}

	private <T> Resp<T> json2DataResp(String result, Class<T> respDataType) {
		if (StringUtils.isBlank(result)) {
			return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, "返回值为空串");
		}

		try {
			JSONObject resultJsonObj = FastJson.jsonStr2Object(result, JSONObject.class);
			String code = resultJsonObj.getString("code");
			String message = resultJsonObj.getString("message");
			Object data = resultJsonObj.get("data");

			if (StringUtils.isBlank(code)) {
				return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, result);
			}
			if (data != null && data instanceof JSONArray) {
				return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, "data字段不是{}类型");
			}
			if (data != null && data instanceof JSONObject) {
				data = ((JSONObject) data).toJavaObject(respDataType);
			}
			return new Resp(code, message, data);
		} catch (JsonException e) {
			return Resp.error(ErrorCode.API_RESULT_PARSE_FAIL, "非json对象格式");
		}
	}

	private Map<String, String> objectParam2MapParam(Object objectParam) {
		Map<String, String> queryParamsMap = Maps.newLinkedHashMap();
		if (objectParam != null) {
			if (objectParam instanceof Map) {
				Map<Object, Object> queryMap = (Map) objectParam;
				for (Entry<Object, Object> entry : queryMap.entrySet()) {
					Object k = entry.getKey();
					Object v = entry.getValue();
					queryParamsMap.put(k.toString(), v == null ? "" : v.toString());
				}
			} else {
				Map<String, Object> queryMap = BeanUtils.bean2Map(objectParam);
				for (Entry<String, Object> entry : queryMap.entrySet()) {
					String k = entry.getKey();
					Object v = entry.getValue();
					queryParamsMap.put(k, v == null ? "" : v.toString());
				}
			}
		}
		return queryParamsMap;
	}

	/**
	 * post请求上传form表单
	 *
	 * @param url            - 请求URL
	 * @param postParams     - 查询参数,如果要保证参数顺序请使用LinkedHashMap
	 * @param headerMap      - 请求头
	 * @param requestTimeout - 请求超时时间,毫秒数
	 * @param encoding
	 * @return
	 * @throws IllegalArgumentException
	 * @throws HttpCallException
	 */
	public String httpCallPostForm(String url, Map<String, String> postParams, Map<String, String> headerMap, int requestTimeout, String encoding) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "Param url must be not null and empty");
		Preconditions.checkArgument(url.startsWith("http"), "Param url must be startWith 'http'");
		Preconditions.checkArgument(postParams != null && !postParams.isEmpty(), "Param postParams must be not null and empty");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

		String urlToSend = url;
		HttpPost httpPost = new HttpPost(urlToSend);
		List<NameValuePair> qparams = Lists.newArrayList();
		for (Entry<String, String> entry : postParams.entrySet()) {
			qparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(qparams, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding:" + encoding);
		}
		httpPost.setEntity(entity);
		/**
		 * 设置超时，覆盖httpClient默认参数
		 */
		if (requestTimeout > 0) {
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(requestTimeout)
					.build();
			httpPost.setConfig(requestConfig);
		}
		/**
		 * 设置请求头
		 */
		if (MapUtils.isNotEmpty(headerMap)) {
			for (Entry<String, String> header : headerMap.entrySet()) {
				httpPost.addHeader(header.getKey(), header.getValue());
			}
		}

		return this.httpCallInternal("httpCallPostForm", httpPost, url, postParams, headerMap, requestTimeout, encoding);
	}

	public String httpCallPostJson(String url, String jsonParams) {
		return httpCallPostJson(url, jsonParams, null, 0, DEFAULT_ENCODING);
	}

	public String httpCallPostJson(String url, String jsonParams, int requestTimeout) {
		return httpCallPostJson(url, jsonParams, null, requestTimeout, DEFAULT_ENCODING);
	}

	public String httpCallPostJson(String url, String jsonParams, int requestTimeout, String encoding) {
		return httpCallPostJson(url, jsonParams, null, requestTimeout, encoding);
	}

	public String httpCallPostJson(String url, String jsonParams, Map<String, String> headerMap) {
		return httpCallPostJson(url, jsonParams, headerMap, 0, DEFAULT_ENCODING);
	}

	public String httpCallPostJson(String url, String jsonParams, Map<String, String> headerMap, int requestTimeout) {
		return httpCallPostJson(url, jsonParams, headerMap, requestTimeout, DEFAULT_ENCODING);
	}

	public <T> T httpCallPostJson(String url, Object jsonParams, Class<T> respType) {
		return httpCallPostJson(url, jsonParams, null, 0, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostJson(String url, Object jsonParams, int requestTimeout, Class<T> respType) {
		return httpCallPostJson(url, jsonParams, null, requestTimeout, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostJson(String url, Object jsonParams, int requestTimeout, String encoding, Class<T> respType) {
		return httpCallPostJson(url, jsonParams, null, requestTimeout, encoding, respType);
	}

	public <T> T httpCallPostJson(String url, Object jsonParams, Map<String, String> headerMap, Class<T> respType) {
		return httpCallPostJson(url, jsonParams, headerMap, 0, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostJson(String url, Object jsonParams, Map<String, String> headerMap, int requestTimeout, Class<T> respType) {
		return httpCallPostJson(url, jsonParams, headerMap, requestTimeout, DEFAULT_ENCODING, respType);
	}

	public <T> T httpCallPostJson(String url, Object jsonParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respType) {
		String jsonStr = "";
		if (jsonParams != null) {
			if (jsonParams instanceof String) {
				jsonStr = (String) jsonParams;
			} else {
				jsonStr = FastJson.object2JsonStr(jsonParams);
			}
		}
		String result = httpCallPostJson(url, jsonStr, headerMap, requestTimeout, encoding);
		return Strings.isNullOrEmpty(result) ? null : FastJson.jsonStr2Object(result, respType);
	}

	public <T> Resp<T> httpCallPostJsonRtnResp(String url, Object jsonParams, Class<T> respDataType) {
		return httpCallPostJsonRtnResp(url, jsonParams, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostJsonRtnResp(String url, Object jsonParams, int requestTimeout, Class<T> respDataType) {
		return httpCallPostJsonRtnResp(url, jsonParams, null, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostJsonRtnResp(String url, Object jsonParams, int requestTimeout, String encoding, Class<T> respDataType) {
		return httpCallPostJsonRtnResp(url, jsonParams, null, requestTimeout, encoding, respDataType);
	}

	public <T> Resp<T> httpCallPostJsonRtnResp(String url, Object jsonParams, Map<String, String> headerMap, Class<T> respDataType) {
		return httpCallPostJsonRtnResp(url, jsonParams, headerMap, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostJsonRtnResp(String url, Object jsonParams, Map<String, String> headerMap, int requestTimeout, Class<T> respDataType) {
		return httpCallPostJsonRtnResp(url, jsonParams, headerMap, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<T> httpCallPostJsonRtnResp(String url, Object jsonParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respDataType) {
		String jsonStr = "";
		if (jsonParams != null) {
			if (jsonParams instanceof String) {
				jsonStr = (String) jsonParams;
			} else {
				jsonStr = FastJson.object2JsonStr(jsonParams);
			}
		}
		String result = httpCallPostJson(url, jsonStr, headerMap, requestTimeout, encoding);
		return json2DataResp(result, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostJsonRtnListDataResp(String url, Object jsonParams, Class<T> respDataType) {
		return httpCallPostJsonRtnListDataResp(url, jsonParams, null, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostJsonRtnListDataResp(String url, Object jsonParams, int requestTimeout, Class<T> respDataType) {
		return httpCallPostJsonRtnListDataResp(url, jsonParams, null, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostJsonRtnListDataResp(String url, Object jsonParams, int requestTimeout, String encoding, Class<T> respDataType) {
		return httpCallPostJsonRtnListDataResp(url, jsonParams, null, requestTimeout, encoding, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostJsonRtnListDataResp(String url, Object jsonParams, Map<String, String> headerMap, Class<T> respDataType) {
		return httpCallPostJsonRtnListDataResp(url, jsonParams, headerMap, 0, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostJsonRtnListDataResp(String url, Object jsonParams, Map<String, String> headerMap, int requestTimeout, Class<T> respDataType) {
		return httpCallPostJsonRtnListDataResp(url, jsonParams, headerMap, requestTimeout, DEFAULT_ENCODING, respDataType);
	}

	public <T> Resp<List<T>> httpCallPostJsonRtnListDataResp(String url, Object jsonParams, Map<String, String> headerMap, int requestTimeout, String encoding, Class<T> respDataType) {
		String jsonStr = "";
		if (jsonParams != null) {
			if (jsonParams instanceof String) {
				jsonStr = (String) jsonParams;
			} else {
				jsonStr = FastJson.object2JsonStr(jsonParams);
			}
		}
		String result = httpCallPostJson(url, jsonStr, headerMap, requestTimeout, encoding);
		return json2ListDataResp(result, respDataType);
	}

	/**
	 * post请求上传json
	 *
	 * @param url            - 请求URL
	 * @param jsonParams     - 查询参数,如果要保证参数顺序请使用LinkedHashMap
	 * @param headerMap      - 请求头
	 * @param requestTimeout - 请求超时时间,毫秒数
	 * @param encoding
	 * @return
	 * @throws IllegalArgumentException
	 * @throws HttpCallException
	 */
	public String httpCallPostJson(String url, String jsonParams, Map<String, String> headerMap, int requestTimeout, String encoding) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "Param url must be not null and empty");
		Preconditions.checkArgument(url.startsWith("http"), "Param url must be startWith 'http'");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(jsonParams), "Param jsonParams must be not null and empty");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(encoding), "Param encoding must be not null and empty");

		String urlToSend = url;
		HttpPost httpPost = new HttpPost(urlToSend);
		StringEntity entity = new StringEntity(jsonParams, encoding);
		// entity.setContentType("application/json;charset=" + encoding);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		/**
		 * 设置超时，覆盖httpClient默认参数
		 */
		if (requestTimeout > 0) {
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					.setSocketTimeout(requestTimeout)
					.build();
			httpPost.setConfig(requestConfig);
		}
		/**
		 * 设置请求头
		 */
		if (MapUtils.isNotEmpty(headerMap)) {
			for (Entry<String, String> header : headerMap.entrySet()) {
				httpPost.addHeader(header.getKey(), header.getValue());
			}
		}
		return this.httpCallInternal("httpCallPostJson", httpPost, url, jsonParams, headerMap, requestTimeout, encoding);
	}

	public CloseableHttpClient getCloseableHttpClient() {
		return closeableHttpClient;
	}

	/**
	 *********************** 默认客户端级别参数,request级别可覆盖<br>
	 * 注意：这些变量的set方法中会修改客户端级别参数且是线程安全的,不需要额外同步,因为DefaultHttpClient默认使用的是SyncBasicHttpParams
	 */
	/**
	 * 链接相关
	 *
	 * @See org.apache.http.params.CoreConnectionPNames
	 */
	private boolean tcpNodelay = true;
	private int soLinger = -1;
	private boolean soReuseaddr = true;
	private int socketBufferSize = 8 * 1024;// 8K
	private boolean keepAlive = true;
	/**
	 * 链接管理相关
	 */
	private int maxPerRoute = 50;// 每个路由连接的最大数量
	private int maxTotal = 200;// 所有路由连接的最大数量

	public boolean isTcpNodelay() {
		return tcpNodelay;
	}

	public void setTcpNodelay(boolean tcpNodelay) {
		this.tcpNodelay = tcpNodelay;
		SocketConfig socketConfig = SocketConfig.copy(defaultSocketConfig).setTcpNoDelay(tcpNodelay).build();
		this.poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
	}

	public int getSoLinger() {
		return soLinger;
	}

	public void setSoLinger(int soLinger) {
		this.soLinger = soLinger;
		SocketConfig socketConfig = SocketConfig.copy(defaultSocketConfig).setSoLinger(soLinger).build();
		this.poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
	}

	public boolean isSoReuseaddr() {
		return soReuseaddr;
	}

	public void setSoReuseaddr(boolean soReuseaddr) {
		this.soReuseaddr = soReuseaddr;
		SocketConfig socketConfig = SocketConfig.copy(defaultSocketConfig).setSoReuseAddress(soReuseaddr).build();
		this.poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
	}

	public int getSocketBufferSize() {
		return socketBufferSize;
	}

	public void setSocketBufferSize(int socketBufferSize) {
		this.socketBufferSize = socketBufferSize;
		ConnectionConfig connectionConfig = ConnectionConfig.copy(defaultConnectionConfig).setBufferSize(socketBufferSize).build();
		this.poolingHttpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
		SocketConfig socketConfig = SocketConfig.copy(this.defaultSocketConfig).setSoKeepAlive(keepAlive).build();
		this.poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
	}

	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
		this.poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
		this.poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
	}

	/**
	 * 定期回收连接池中失效链接线程<br>
	 * <p>
	 * 虽然连接池有了，但是由于http连接的特殊性（只有在通讯正在进行（block）时才能够对IO事件做出反应）<br>
	 * 一旦连接被放回连接池后，我们无从知道该连接是否还是keepalive的，<br>
	 * 且此时也无法监控当前socket的状态（即服务器主动关闭了连接，但客户端没有通讯时是不知道当前连接的状态是怎样的）。<br>
	 * 怎么办呢？httpClient采用了一个折中的方案来检查连接的“状态”， 就是由客户端自己通过配置去主动关闭其认为是失效的连接。<br>
	 * <p>
	 */
	private static class IdleConnectionMonitorThread extends Thread {
		private final Set<HttpClientConnectionManager> connMgrSet = Sets.newHashSet();
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread() {
			super("HttpClientUtils$IdleConnectionMonitorThread");
		}

		public void addConnMgr(HttpClientConnectionManager connectionManager) {
			connMgrSet.add(connectionManager);
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						// 如果在3秒内有其他线程持有this锁且不释放,将不会自动唤醒
						wait(3000L);
						// 关闭过期连接
						for (HttpClientConnectionManager connMgr : connMgrSet) {
							connMgr.closeExpiredConnections();
							// 可选地，关闭空闲超过60秒的连接
							connMgr.closeIdleConnections(DEFAULT_KEEPALIVE_TIME, TimeUnit.MILLISECONDS);
						}
					}
				}
			} catch (InterruptedException ex) {
				// 终止
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}

	/**
	 * 连接保持活动的策略<br>
	 * HTTP规范没有确定一个持久连接可能或应该保持活动多长时间。<br>
	 * 一些HTTP服务器使用非标准的头部信息Keep-Alive来告诉客户端它们想在服务器端保持连接活动的周期秒数。<br>
	 * 如果这个信息可用，HttClient就会利用这个它。如果头部信息Keep-Alive在响应中不存在，HttpClient假设连接无限期的保持活动。<br>
	 * 然而许多现实中的HTTP服务器配置了在特定不活动周期之后丢掉持久连接来保存系统资源，往往这是不通知客户端的。<br>
	 * 如果默认的策略证明是过于乐观的，那么就会有人想提供一个定制的保持活动策略。 <br>
	 * 为了使connMgr.closeExpiredConnections()起到作用需要指定连接keep alive策略告诉httpClient哪些连接大概什么时候会过期,可以关闭<br>
	 */
	private static class HttpConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy {

		@Override
		public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
			/**
			 * 兑现服务器返回的keep-alive头部信息
			 */
			long keepAlive = super.getKeepAliveDuration(response, context);
			if (keepAlive == -1) {
				/**
				 * 不同目标主机采取不同的keepalive策略
				 */
				HttpHost target = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
				if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
					keepAlive = 5 * 1000;// 保持活动5秒
				} else {
					keepAlive = DEFAULT_KEEPALIVE_TIME;// 保持活动60秒
				}
			}

			return keepAlive;
		}
	}

	/**
	 * 请求重试的策略<br>
	 * 继承默认的重试策略,针对httpClietn4.2.1的bug:<br>
	 * DefaultHttpResponseParser.parseHead 95行<br>
	 * 偶尔会有NoHttpResponseException("The target server failed to respond")抛出
	 */
	private static class HttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {

		public HttpRequestRetryHandler() {
			super();
		}

		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			boolean retry = super.retryRequest(exception, executionCount, context);

			if (exception instanceof NoHttpResponseException) {
				log.info("HttpClientUtils.retryRequest detected NoHttpResponseException,sleep 1 ml,then retry");
				try {
					Thread.sleep(1L);// 等待1毫秒
				} catch (InterruptedException e) {
				}
				if (executionCount > super.getRetryCount()) {
					retry = false;
				} else {
					retry = true;
				}
			}
			return retry;
		}
	}

	private static String print(Object param) {
		if (param == null) {
			return "";
		} else {
			return param.toString();
		}
	}
}
