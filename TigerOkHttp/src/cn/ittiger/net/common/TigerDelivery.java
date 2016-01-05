package cn.ittiger.net.common;

import cn.ittiger.net.http.exeception.TigerHttpException;
import android.os.Handler;
import android.os.Looper;

/**
 * 异步结果分发，单例
 * @auther: hyl
 * @time: 2015-9-2上午11:07:29
 */
public class TigerDelivery {
	private static TigerDelivery delivery;
	private Handler mDeliveryHandler;
	
	private TigerDelivery() {
		mDeliveryHandler = new Handler(Looper.getMainLooper());;
	}
	
	public static TigerDelivery get() {
		if(delivery == null) {
			synchronized (TigerDelivery.class) {
				if(delivery == null) {
					delivery = new TigerDelivery();
				}
			}
		}
		return delivery;
	}

	/**
	 * 请求执行之前的响应分发
	 * @param callback
	 */
	public <T> void deliveryOnPreExecute(final RequestCallback<T> callback) {
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onPreExecute();
				}
			}
		});
	}
	
	/**
	 * 请求成功之后对结果进行分发
	 * @param result
	 * @param callback
	 */
	public <T> void deliverySuccessResult(final T result, final RequestCallback<T> callback) {
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onSuccess(result);
					callback.onAfterExecute();
				}
			}
		});
	}
	
	/**
	 * 请求失败之后的响应分发
	 * @param req
	 * @param e
	 * @param callback
	 */
	public <T> void deliveryFailureResult(final TigerHttpException e, final RequestCallback<T> callback) {
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onFailure(e);
					callback.onAfterExecute();
				}
			}
		});
	}
	
	/**
	 * 请求失败之后的响应分发
	 * @param req
	 * @param e
	 * @param callback
	 */
	public <T> void deliveryOnLoading(final long totalCount, final long currCount, final RequestCallback<T> callback) {
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onLoading(totalCount, currCount);
				}
			}
		});
	}
}
