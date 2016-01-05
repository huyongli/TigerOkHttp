package cn.ittiger.net.http.request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.ittiger.net.common.RequestCallback;
import cn.ittiger.net.http.config.CacheStrategy;
import cn.ittiger.net.http.parser.TigerParser;

/**
 * 请求对象，<T>为请求的返回结果
 * Http请求基类
 * 文件上传请求请使用已经定义好的{@link}TigerUploadRequest，文件下载请求请使用已经定义好的{@link}TigerDownloadRequest
 * @auther: hyl
 * @time: 2015-9-10上午10:20:17
 */
public abstract class TigerRequest<T> {
	/**
	 * 请求地址
	 */
	private String url;
	/**
	 * 请求参数
	 * 参数值编码问题处理说明：OKHttp中参数都默认采用的是UTF-8编码方式进行参数编码传输
	 * 1.get请求时，对于中文参数不需要手动进行编码，服务端只需要对tomcat设置默认编码方式(URIEncoding="UTF-8" )为UTF-8即可
	 * 2.post请求，OkHttp会对非ASCII码字符进行UTF-8编码，ASCII字符不进行编码，因此post请求参数有两种编码处理方式
	 * 第一种：在传参时手动进行编码(encode(str, "UTF-8"))，这样OKHttp就不会再进行编码，然后自己在服务端进行解码(decode(str, "UTF-8"))
	 * 第二种，在传参时自己不手动编码，让OKHttp自己处理，然后在服务端设置客户端请求重新编码的编码方式，如：req.setCharacterEncoding("UTF-8");
	 */
	private Map<String, Object> params;
	/**
	 * 异步请求过程中的回调
	 */
	protected RequestCallback<T> requestCallback;
	/**
	 * 请求头信息
	 */
	private Map<String, String> headers;
	/**
	 * 当前请求标签，可用于取消当前请求，默认值为自动生成的Guid
	 */
	private Object tag;
	/**
	 * 是否缓存当次请求，默认缓存
	 */
	private CacheStrategy cacheStrategy = CacheStrategy.DEFAULT;

	public TigerRequest(String url) {
		this.url = url;
		this.tag = UUID.randomUUID();
	}
	
	/**
	 * 为当前请求添加一个请求头信息
	 * @param key
	 * @param value
	 * @return 返回请求对象自己
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerRequest<T>> S addHeader(String key, String value) {
		if(headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put(key, value);
		return (S) this;
	}
	
	/**
	 * 为当前请求添加一个请求参数
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerRequest<T>> S addParam(String key, Object value) {
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		params.put(key, value);
		return (S) this;
	}
	
	/**
	 * 获取当前请求的请求地址
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 设置当前请求的请求地址
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerRequest<T>> S setUrl(String url) {
		this.url = url;
		return (S) this;
	}
	
	/**
	 * 获取当前请求的请求参数
	 * @return
	 */
	public Map<String, Object> getParams() {
		return params;
	}
	
	/**
	 * 设置当前请求的请求参数
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerRequest<T>> S setParams(Map<String, Object> params) {
		this.params = params;
		return (S) this;
	}
	
	/**
	 * 获取当前请求的标签
	 * @return
	 */
	public Object getTag() {
		return tag;
	}
	
	/**
	 * 设置当前请求的标签，用于取消请求时用
	 * @param tag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerRequest<T>> S setTag(Object tag) {
		this.tag = tag;
		return (S) this;
	}

	/**
	 * 获取设置的当前请求的请求头信息
	 * @return
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * 获取当前请求的缓存策略
	 * @return
	 */
	public CacheStrategy getCacheStrategy() {
		return cacheStrategy;
	}

	/**
	 * 设置当前请求的缓存策略
	 * @param cache
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerRequest<T>> S setCacheStrategy(CacheStrategy cacheStrategy) {
		this.cacheStrategy = cacheStrategy;
		return (S) this;
	}

	/**
	 * 获取此次请求的回调监听
	 * @return
	 */
	public RequestCallback<T> getRequestCallback() {
		return requestCallback;
	}

	/**
	 * 设置此次请求过程中的回调监听(只有异步请求才有效)
	 * @param requestCallback
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerRequest<T>> S setRequestCallback(RequestCallback<T> requestCallback) {
		this.requestCallback = requestCallback;
		return (S) this;
	}

	/**
	 * 请求结果数据解析器
	 * @return
	 */
	public abstract TigerParser<T> getDataParser();
}
