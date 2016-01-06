package cn.ittiger.net.http.request;

import java.lang.reflect.Type;

import cn.ittiger.net.http.parser.TigerJsonParser;
import cn.ittiger.net.http.parser.TigerParser;

/**
 * JSON结果请求
 * 服务返回为JSON字符串，此请求会自动将JSON字符串转换指定的JavaBean对象或是String
 * @auther: hyl
 * @time: 2015-12-30下午3:20:53
 */
public class TigerJsonRequest<T> extends TigerRequest<T> {
	/**
	 * 结果返回类型，与泛型T相对应(同步请求必须设置)
	 */
	protected Type resultType;
	/**
	 * 结果解析器
	 */
	protected TigerParser<T> dataParser;

	public TigerJsonRequest(String url) {
		super(url);
	}
	
	/**
	 * @param url
	 * @param resultType	结果返回类型，与泛型T相对应(同步请求必须设置)
	 */
	public TigerJsonRequest(String url, Type resultType) {
		super(url);
		this.resultType = resultType;
	}
	
	/**
	 * 设置结果返回类型，同步请求设置
	 * @param resultType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerJsonRequest<T>> S setResultType(Type resultType) {
		this.resultType = resultType;
		return (S) this;
	}

	@Override
	public TigerParser<T> getDataParser() {
		if(dataParser == null) {
			dataParser = new TigerJsonParser<T>(this, resultType);
		}
		return dataParser;
	}

	/**
	 * 设置结果解析器
	 * @param dataParser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends TigerJsonRequest<T>> S setDataParser(TigerParser<T> dataParser) {
		this.dataParser = dataParser;
		return (S) this;
	}
}
