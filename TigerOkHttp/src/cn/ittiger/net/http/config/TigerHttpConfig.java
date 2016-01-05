package cn.ittiger.net.http.config;

import java.net.CookieManager;
import java.net.CookiePolicy;

import android.annotation.SuppressLint;
import android.content.Context;
import cn.ittiger.net.common.WrapperJson;

/**
 * OkHttpClient初始化时的配置信息
 * 通过OkHttpManager.setHttpConfig(OkHttpConfig config)设置，仅在第一次调用的时候起作用
 * @auther: hyl
 * @time: 2015-8-31下午4:40:38
 */
@SuppressLint("NewApi")
public class TigerHttpConfig {
	/**
	 * 默认读取超时时间--30s
	 */
	private static final int DEFAULT_READTIMEOUT = 30;
	/**
	 * 默认写超时时间--15s
	 */
	private static final int DEFAULT_WRITETIMEOUT = 15;
	/**
	 * 默认连接超时时间--15s
	 */
	private static final int DEFAULT_CONNECTTIMEOUT = 15;
	/**
	 * cookie管理策略 
	 */
	public CookieManager cookieHandler = new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
	/**
	 * 数据读取超时时间，默认为30s
	 */
	public int readTimeOut = DEFAULT_READTIMEOUT;
	/**
	 * 写超时时间，默认为15s
	 */
	public int writeTimeOut = DEFAULT_WRITETIMEOUT;
	/**
	 * 连接超时时间，默认为15s
	 */
	public int connectTimeOut = DEFAULT_CONNECTTIMEOUT;
	/**
	 * 缓存默认大小
	 */
	public int cacheSize = 10 * 1024 * 1024;
	/**
	 * 缓存目录，默认为系统给应用分配的缓存目录
	 */
	public String cacheDirectory;
	/**
	 * 是否对结果进行了包装，默认为是
	 */
	public boolean isWrapperResult = true;
	/**
	 * 请求结果的包装结构类型
	 */
	public WrapperJson wrapperJsonResult;
	
	public TigerHttpConfig(Context context) {
		cacheDirectory = context.getCacheDir().getAbsolutePath();
		wrapperJsonResult = new WrapperJson();
	}
}
