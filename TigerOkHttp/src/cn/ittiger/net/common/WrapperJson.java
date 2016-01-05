package cn.ittiger.net.common;

/**
 * 对于请求结果的封装结构(Json结构)
 * @author: huylee
 * @time:	2015-9-8下午9:54:53
 */
public class WrapperJson {
	/**
	 * 请求成功与否的状态码字段
	 */
	public String code_name = "flag";
	/**
	 * 请求失败的状态码值，非此值均为成功
	 */
	public int code_error_value = 0;
	/**
	 * 请求结果字段
	 */
	public String result_name = "result";
	/**
	 * 请求失败时的错误信息，仅在请求失败时该字段才有值
	 */
	public String error_name = "error";
}
