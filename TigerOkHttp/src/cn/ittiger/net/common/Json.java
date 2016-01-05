package cn.ittiger.net.common;

import com.google.gson.Gson;

/**
 * Gson单例对象
 * @author: huylee
 * @time:	2015-9-8下午9:48:17
 */
public final class Json { 
	private static Json json;
	private Gson gson;
	
	public Json() {
		gson = new Gson();
	}
	
	public static Gson getGson() {
		if(json == null) {
			synchronized (Json.class) {
				if(json == null) {
					json = new Json();
				}
			}
		}
		return json.gson;
	}
}
