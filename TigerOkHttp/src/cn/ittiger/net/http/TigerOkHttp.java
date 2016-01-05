package cn.ittiger.net.http;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.ittiger.net.http.config.TigerHttpConfig;
import cn.ittiger.net.http.exeception.TigerHttpException;
import cn.ittiger.net.http.request.TigerRequest;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

/**
 * OKHttp请求管理类，单例模式实现，建议在使用之前在Application中调用{@link init(OkHttpConfig config)}进行初始化
 * 
 * 文件上传请求请使用已经定义好的{@link}TigerUploadRequest，文件下载请求请使用已经定义好的{@link}TigerDownloadRequest
 * @auther: hyl
 * @time: 2015-8-31上午11:38:13
 */
public final class TigerOkHttp {
	/**
	 * OkHttpManager单例对象，保证OKHttpClient全局唯一实例
	 */
	private static TigerOkHttp mOkHttpManager;
	/**
	 * 
	 */
	private OkHttpClient mOkHttpClient;
	/**
	 * OkHttp相关配置信息
	 */
	private TigerHttpConfig mConfig;
	/**
	 * OkHttp请求执行器
	 */
	private TigerOkHttpExecutor mOkHttpExecutor;
	
	private TigerOkHttp(TigerHttpConfig config) {
		mOkHttpClient = new OkHttpClient();
		mConfig = config;
		mOkHttpExecutor = new TigerOkHttpExecutor(mOkHttpClient);
	}
	
	/**
	 * 获取单例对象，建议在使用之前调用此方法进行初始化
	 * @param config
	 * @return
	 */
	public static TigerOkHttp init(TigerHttpConfig config) {
		if(mOkHttpManager == null) {
			synchronized (TigerOkHttp.class) {
				if(mOkHttpManager == null) {
					mOkHttpManager = new TigerOkHttp(config);
					mOkHttpManager.initOkHttpConfig();
				}
			}
		}
		return mOkHttpManager;
	}
	
	private void initOkHttpConfig() {
		this.mOkHttpClient.setCookieHandler(mConfig.cookieHandler);
		this.mOkHttpClient.setConnectTimeout(mConfig.connectTimeOut, TimeUnit.SECONDS);
		this.mOkHttpClient.setReadTimeout(mConfig.readTimeOut, TimeUnit.SECONDS);
		this.mOkHttpClient.setWriteTimeout(mConfig.writeTimeOut, TimeUnit.SECONDS);
		this.mOkHttpClient.setCache(new Cache(new File(mConfig.cacheDirectory), mConfig.cacheSize));
	}

	public static TigerHttpConfig getConfig() {
		return mOkHttpManager.mConfig;
	}
	
	public static TigerOkHttpExecutor getOkHttpExecutor() {
		return mOkHttpManager.mOkHttpExecutor;
	}
	
	public static OkHttpClient getOkHttpClient() {
		return mOkHttpManager.mOkHttpClient;
	}
	
	/**
	 * 同步Get请求
	 * @param 	request
	 * @return						请求成功、解析成功后返回实体对象
	 * @throws TigerHttpException 	请求失败，结果解析失败抛出该异常
	 */
	public static <T> T getSync(TigerRequest<T> request) throws TigerHttpException {
		request.setRequestCallback(null);
		return TigerOkHttp.getOkHttpExecutor().syncExecute(OKHttpBuilder.buildGetRequest(request), request.getDataParser());
	}
	
	/**
	 * 异步Get请求
	 * @param request
	 */
	public static <T> void getAsync(TigerRequest<T> request) {
		TigerOkHttp.getOkHttpExecutor().asyncExecute(OKHttpBuilder.buildGetRequest(request), request.getDataParser());
	}
	
	/**
	 * 同步Post请求
	 * @param 	request
	 * @return						请求成功、解析成功后返回实体对象
	 * @throws TigerHttpException 	请求失败，结果解析失败抛出该异常
	 */
	public static <T> T postSync(TigerRequest<T> request) throws TigerHttpException {
		request.setRequestCallback(null);
		return TigerOkHttp.getOkHttpExecutor().syncExecute(OKHttpBuilder.buildPostRequest(request), request.getDataParser());
	}
	
	/**
	 * 异步Post请求(可以返回单个：T为Class 或 多个：T为List<T>)
	 * @param request
	 */
	public static <T> void postAsync(TigerRequest<T> request) {
		TigerOkHttp.getOkHttpExecutor().asyncExecute(OKHttpBuilder.buildPostRequest(request), request.getDataParser());
	}
	
	/**
	 * 取消某个请求
	 * @author: huylee
	 * @time:	2015-9-10下午9:40:28
	 * @param request	取消某个请求
	 */
	public static <T> void cancel(TigerRequest<T> request) {
		TigerOkHttp.getOkHttpClient().cancel(request.getTag());
	}
	
	/**
	 * 取消指定标签的所有请求
	 * @author: huylee
	 * @time:	2015-9-10下午9:40:28
	 * @param request	取消某个请求
	 */
	public static void cancel(Object tag) {
		TigerOkHttp.getOkHttpClient().cancel(tag);
	}
}
