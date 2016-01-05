package cn.ittiger.net.http.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSource;

import cn.ittiger.net.common.Json;
import cn.ittiger.net.http.exeception.ExceptionStatusCode;
import cn.ittiger.net.http.exeception.TigerHttpException;
import cn.ittiger.net.http.request.TigerRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * JSON解析器，将服务返回的JSON字符串解析成JavaBean对象或JavaBean集合
 * 此解析器使用最多
 * @auther: hyl
 * @time: 2015-12-29下午6:59:12
 */
public class TigerJsonParser<T> extends TigerParser<T> {
	/**
	 * 结果返回的每条数据类型(同步请求必须设置)
	 */
	protected Type resultType;
	
	public TigerJsonParser(TigerRequest<T> request) {
		super(request);
	}

	/**
	 * 同步请求最好直接调用此构造函数，异步请求调用此构造函数设置的返回类型不会起作用
	 * @param request		请求对象
	 * @param resultType	结果返回的每条数据类型(同步请求必须设置)，如果是List的话，此resultType为每一项的数据的类型
	 */
	public TigerJsonParser(TigerRequest<T> request, Type resultType) {
		super(request);
		this.resultType = resultType;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T parser(BufferedSource source) throws TigerHttpException {
		String stringResult = getWrapperResult(source);
		try {
			if(request.getRequestCallback() != null) {//异步请求
				resultType = request.getRequestCallback().getResultType();
				T bean = Json.getGson().fromJson(stringResult, resultType);
				return bean;
			} else {//同步请求结果类型处理
				if(resultType == null) {
					throw new TigerHttpException(ExceptionStatusCode.STATUS_REQUEST_ERROR_PARAMS, "synchronous request must be set resultType");
				}
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(stringResult);
				if(jsonElement.isJsonArray()) {//结果是JSON数组
					List list = new ArrayList();
					JsonArray array = jsonElement.getAsJsonArray();
					for(JsonElement json : array) {
						list.add(Json.getGson().fromJson(json, resultType));
					}
					return (T) list;
				} else {//不是JSON数组
					return Json.getGson().fromJson(jsonElement, resultType);
				}
			}
		} catch (Exception e) {
			if(e instanceof TigerHttpException) {
				throw (TigerHttpException)e;
			} else {
				throw new TigerHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
			}
		}
	}
}
