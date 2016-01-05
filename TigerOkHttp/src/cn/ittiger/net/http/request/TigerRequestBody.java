package cn.ittiger.net.http.request;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import cn.ittiger.net.common.RequestCallback;
import cn.ittiger.net.common.TigerDelivery;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

/**
 * 扩展OkHttp的请求体，实现上传时的进度提示
 * @auther: hyl
 * @time: 2015-9-8下午5:09:09
 */
public final class TigerRequestBody<T> extends RequestBody {
	/**
	 * 实际请求体
	 */
	private RequestBody requestBody;
	/**
	 * 上传回调接口
	 */
	private RequestCallback<T> callback;
    /**
     * 包装完成的BufferedSink
     */
    private BufferedSink bufferedSink;
    
	public TigerRequestBody(RequestBody requestBody, RequestCallback<T> callback) {
		super();
		this.requestBody = requestBody;
		this.callback = callback;
	}

	@Override
	public long contentLength() throws IOException {
		return requestBody.contentLength();
	}
	
	@Override
	public MediaType contentType() {
		return requestBody.contentType();
	}

	@Override
	public void writeTo(BufferedSink sink) throws IOException {
		if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
	}

	/**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                TigerDelivery.get().deliveryOnLoading(contentLength, bytesWritten, callback);
            }
        };
    }
}