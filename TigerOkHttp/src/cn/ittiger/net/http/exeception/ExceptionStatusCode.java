package cn.ittiger.net.http.exeception;

/**
 * 异常状态码
 * @auther: hyl
 * @time: 2015-11-30下午3:24:45
 */
public final class ExceptionStatusCode {
	/**
	 * 未知状态
	 */
	public static final int STATUS_UNKNOWN = 0;
	/**
	 * 请求结果没有安装指定格式进行包装
	 */
	public static final int STATUS_RESULT_FORMAT_ERROR = 1;
	/**
	 * 请求结果中存在服务端错误信息
	 */
	public static final int STATUS_RESULT_EXIST_ERRORINFO = 2;
	/**
	 * 请求失败
	 */
	public static final int STATUS_REQUEST_FAILED = 3;
	/**
	 * 解析结果失败
	 */
	public static final int STATUS_PARSER_RESULT_ERROR = 4;
	/**
	 * 请求IO异常导致失败
	 */
	public static final int STATUS_REQUEST_IO_ERROR = 5;
	/**
	 * 请求参数异常
	 */
	public static final int STATUS_REQUEST_ERROR_PARAMS = 6;
}
