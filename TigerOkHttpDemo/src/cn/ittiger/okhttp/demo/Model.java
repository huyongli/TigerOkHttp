package cn.ittiger.okhttp.demo;

public class Model {
	/**
	 * 参数结果
	 */
	private String result;
	/**
	 * 请求你方法
	 */
	private String method;
	
	public Model() {
	}
	
	@Override
	public String toString() {
		return "{result=" + result + ", method=" + method + "}";
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
