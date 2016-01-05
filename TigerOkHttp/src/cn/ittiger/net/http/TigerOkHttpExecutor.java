package cn.ittiger.net.http;

import java.io.IOException;

import android.util.Log;
import cn.ittiger.net.common.TigerDelivery;
import cn.ittiger.net.http.config.Tag;
import cn.ittiger.net.http.exeception.ExceptionStatusCode;
import cn.ittiger.net.http.exeception.TigerHttpException;
import cn.ittiger.net.http.parser.TigerParser;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * 请求执行器，发起OkHttp请求
 * @auther: hyl
 * @time: 2015-9-10上午11:41:51
 */
public class TigerOkHttpExecutor {
	private OkHttpClient mOkHttpClient;

	public TigerOkHttpExecutor(OkHttpClient okHttpClient) {
		super();
		this.mOkHttpClient = okHttpClient;
	}
	
	/**
	 * 异步执行某个OkHttp请求
	 * @param request
	 * @param parser	请求结果解析器
	 */
	public <T> void asyncExecute(Request request, final TigerParser<T> parser) {
		Log.i(Tag.TAG, "请求地址：" + request.urlString());
		if(parser == null) {
			throw new IllegalArgumentException("not set Data Parser");
		}
		TigerDelivery.get().deliveryOnPreExecute(parser.getTigerRequest().getRequestCallback());
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Response response) throws IOException {
				parser.parserAsync(response);
			}
			
			@Override
			public void onFailure(Request req, IOException e) {
				TigerDelivery.get().deliveryFailureResult(new TigerHttpException(ExceptionStatusCode.STATUS_REQUEST_IO_ERROR, e), parser.getTigerRequest().getRequestCallback());
			}
		});
	}
	
	/**
	 * 同步执行某个OkHttp请求，并返回实体对象
	 * @param request				
	 * @return						请求解析成功返回实体对象
	 * @throws TigerHttpException 	请求失败、解析失败均抛出异常
	 */
	public <T> T syncExecute(Request request, TigerParser<T> parser) throws TigerHttpException {
		Log.i(Tag.TAG, "请求地址：" + request.urlString());
		if(parser == null) {
			throw new IllegalArgumentException("not set Data Parser");
		}
		Response response = null;
		try {
			response = mOkHttpClient.newCall(request).execute();
		} catch (IOException e) {
			throw new TigerHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, e.getMessage());
		}
		return parser.parserSync(response);
	}
}
