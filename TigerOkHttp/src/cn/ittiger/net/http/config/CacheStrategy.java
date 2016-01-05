package cn.ittiger.net.http.config;

/**
 * 缓存策略
 * @auther: hyl
 * @time: 2015-8-31下午4:27:34
 */
public enum CacheStrategy {
	/**
	 * 默认策略，请求结果会缓存起来，下次请求如果存在缓存在从缓存中取结果，没有则请求服务
	 */
	DEFAULT,
	/**
	 * 无缓存，强制使用网络请求数据
	 */
	FORCE_NETWORK,
	/**
	 * 强制使用缓存，如果存在缓存则从缓存中取数据，如果不存在该请求的缓存，则请求失败(504 Unsatisfiable Request)
	 */
	FORCE_CACHE,
}
