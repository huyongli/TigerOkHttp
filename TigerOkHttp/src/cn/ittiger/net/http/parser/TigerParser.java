package cn.ittiger.net.http.parser;

import static com.squareup.okhttp.internal.Util.UTF_8;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONObject;

import okio.BufferedSource;

import cn.ittiger.net.common.TigerDelivery;
import cn.ittiger.net.http.TigerOkHttp;
import cn.ittiger.net.http.exeception.ExceptionStatusCode;
import cn.ittiger.net.http.exeception.TigerHttpException;
import cn.ittiger.net.http.request.TigerRequest;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;

/**
 * OkHttp请求响应解析器
 * 同步请求请调用无参构造函数，异步请求请调用有参构造函数
 * 如果要自定义自己的解析器，只需要继承此类，重写方法parser(BufferedSource source)即可
 * @auther: hyl
 * @time: 2015-9-9下午2:52:08
 */
public abstract class TigerParser<T> {
	/**
	 * 当前请求的请求监听，异步请求才有用
	 */
	protected TigerRequest<T> request;
	/**
	 * 返回字符串结果时的内容长度
	 */
	protected long contentLength;
	/**
	 * 当次请求的编码名称
	 */
	protected String charsetName;
	/**
	 * 同步请求的解析调用此构造函数
	 */
	public TigerParser(TigerRequest<T> request) {
		this.request = request;
	}
	
	/**
	 * 解析OkHttp同步请求响应，此请求返回单个实体对象
	 * @param response				待解析的请求响应体
	 * @param claxx					返回的实体对象Class对象
	 * @return						解析成功返回{@link List}
	 * @throws TigerHttpException 	请求失败状态、解析失败均抛出异常
	 */
	public final T parserSync(Response response) throws TigerHttpException {
		if(response.isSuccessful()) {
			try {
				getResponseBodyInfo(response.body());
				return parser(response.body().source());
			} catch (IOException e) {
				throw new TigerHttpException(ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR, e.getMessage());
			}
		} else {
			throw new TigerHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, response.message());
		}
	}
	
	/**
	 * 解析OkHttp异步请求响应
	 * @param response		待解析的请求响应体
	 */
	public final void parserAsync(Response response) {
		if(response.isSuccessful()) {
			try {
				getResponseBodyInfo(response.body());
				T data = parser(response.body().source());
				TigerDelivery.get().deliverySuccessResult(data, request.getRequestCallback());
			} catch (IOException e) {
				TigerDelivery.get().deliveryFailureResult(new TigerHttpException(ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR, e.getMessage()), request.getRequestCallback());
			} catch (TigerHttpException e) {
				TigerDelivery.get().deliveryFailureResult(e, request.getRequestCallback());
			}
		} else {
			TigerDelivery.get().deliveryFailureResult(new TigerHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, response.message()), request.getRequestCallback());
		}
	}
	
	/**
	 * 获取此次请求的编码方式和内容长度
	 * @param body
	 * @throws IOException
	 */
	private final void getResponseBodyInfo(ResponseBody body) throws IOException {
		MediaType contentType = body.contentType();
	    Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
		this.charsetName = charset.name();
		this.contentLength = body.contentLength();
	}
	
	/**
	 * 解析OkHttp请求的结果流，并返回解析得到的实体对象
	 * @param source	待解析的响应结果流
	 * @return
	 * @throws TigerHttpException 	解析失败抛出异常
	 */
	public abstract T parser(BufferedSource source) throws TigerHttpException;
	
	/**
	 * 返回请求对象
	 * @return
	 */
	public TigerRequest<T> getTigerRequest() {
		return request;
	}
	
	/**
	 * 获取请求返回的结果
	 * @param response				待解析的请求响应体
	 * @return						返回请求响应字符串
	 * @throws TigerHttpException 	请求失败状态、解析失败均抛出异常
	 */
	public final String getWrapperResult(BufferedSource source) throws TigerHttpException {
		try {
		    if(contentLength > Integer.MAX_VALUE) {
		    	throw new IOException("Cannot buffer entire body for content length: " + contentLength);
		    }
		    byte[] bytes;
		    try {
		    	bytes = source.readByteArray();
		    } finally {
		    	Util.closeQuietly(source);
		    }
		    if(contentLength != -1 && contentLength != bytes.length) {
		    	throw new IOException("Content-Length and stream length disagree");
		    }
		    String bodyString = new String(bytes, charsetName);
		    return getWrapperResult(bodyString);
		} catch (Exception e) {
			if(e instanceof TigerHttpException) {
				throw (TigerHttpException)e;
			} else {
				throw new TigerHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
			}
		}
	}
	
	/**
	 * 获取请求返回的结果
	 * @param bodyString	待解析的结果字符串
	 * @return
	 * @throws TigerHttpException
	 */
	private final String getWrapperResult(String bodyString) throws TigerHttpException {
		try {
			if(!TigerOkHttp.getConfig().isWrapperResult) {//结果没有进行包装过
				return bodyString;
			}
			JSONObject json = new JSONObject(bodyString);
			if(!json.has(TigerOkHttp.getConfig().wrapperJsonResult.code_name)) {
				throw new TigerHttpException(ExceptionStatusCode.STATUS_RESULT_FORMAT_ERROR, "结果没有进行包装，无法以包装结果进行解析");
			}
			int flag = json.getInt(TigerOkHttp.getConfig().wrapperJsonResult.code_name);
			if(flag == TigerOkHttp.getConfig().wrapperJsonResult.code_error_value) {//当前返回了错误状态码
				throw new TigerHttpException(ExceptionStatusCode.STATUS_RESULT_EXIST_ERRORINFO, json.getString(TigerOkHttp.getConfig().wrapperJsonResult.error_name));
			}
			return json.getString(TigerOkHttp.getConfig().wrapperJsonResult.result_name);
		} catch (Exception e) {
			if(e instanceof TigerHttpException) {
				throw (TigerHttpException)e;
			} else {
				throw new TigerHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
			}
		}
	}
}
