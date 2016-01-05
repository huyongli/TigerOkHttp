package cn.ittiger.net.http;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import cn.ittiger.net.http.request.TigerRequest;
import cn.ittiger.net.http.request.TigerRequestBody;
import cn.ittiger.net.http.request.TigerUploadRequest;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * 请求、响应构造器
 * @auther: hyl
 * @time: 2015-8-31下午5:21:31
 */
public class OKHttpBuilder {
	/**
	 * 流请求媒体类型
	 */
	private final static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    /**
     * 字符串请求类型
     */
    private final static MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    
    /*------------------------------------ 构造Request部分 --------------------------------------------*/
    
	/**
	 * 根据请求url构造请求
	 * @param url		请求url
	 * @param headers	请求头信息
	 * @return
	 */
	public static Request.Builder buildRequest(String url, Map<String, String> headers) {
		if(headers != null && headers.size() > 0) {
			Request.Builder builder = new Request.Builder().url(buildUrl(url, null));
			for(String key : headers.keySet()) {
				builder.addHeader(key, headers.get(key));
			}
			return builder;
		} else {
			return new Request.Builder().url(buildUrl(url, null));
		}
	}
	
	/**
	 * 根据请求url和请求参数构造get请求对象
	 * @param url		请求url
	 * @param params	请求参数
	 * @return
	 */
	public static Request.Builder buildGetRequest(String url, Map<String, Object> params, Map<String, String> headers) {
		return buildRequest(buildUrl(url, params), headers);
	}
	
	/**
	 * 根据请求url和请求参数构造一个新的url(将参数添加到url上)
	 * @author: huylee
	 * @time:	2015-9-7下午10:17:22
	 * @param url		请求url
	 * @param params	请求参数
	 * @return
	 */
	public static String buildUrl(String url, Map<String, Object> params) {
		if(url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("请求URL不能为null");
		}
		StringBuilder sb = new StringBuilder(url);
		if(params != null) {
			for(String key : params.keySet()) {
				sb.append(key);
				sb.append("=");
				sb.append(params.get(key).toString());
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/**
	 * 根据请求url和请求体构造请求(Post请求)
	 * @param url		请求url
	 * @param body		请求体
	 * @param headers 	请求头信息
	 * @return
	 */
	public static Request.Builder buildPostRequest(String url, RequestBody body, Map<String, String> headers) {
		if(url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("请求URL不能为null");
		}
		if(body == null) {
			return buildRequest(url, headers);
		}
		if(headers != null && headers.size() > 0) {
			Request.Builder builder = new Request.Builder().url(buildUrl(url, null));
			for(String key : headers.keySet()) {
				builder.addHeader(key, headers.get(key));
			}
			return builder.post(body);
		} else {
			return new Request.Builder().url(url).post(body);
		}
	}
	
	/**
	 * 根据请求url和请求参数构造Post请求对象
	 * @param url		请求url
	 * @param params	请求参数
	 * @param headers 	请求头信息
	 * @return
	 */
	public static Request.Builder buildPostRequest(String url, Map<String, Object> params, Map<String, String> headers) {
		RequestBody body = buildRequestBody(params);
		return buildPostRequest(url, body, headers);
	}
	
	/*------------------------------------ 构造RequestBody部分 --------------------------------------------*/
	
	/**
	 * 根据请求参数构造Post请求体
	 * @param params
	 * @return
	 */
	public static RequestBody buildRequestBody(Map<String, Object> params) {
		if(params == null) {
			return null;
		}
		FormEncodingBuilder builder = new FormEncodingBuilder();
		for(String key : params.keySet()) {
			builder.add(key, params.get(key).toString());
		}
		return builder.build();
	}
	
	/**
	 * 根据多个文件和其他Form参数构造多文件上传请求体
	 * @param files				文件数组
	 * @param formParams		其他附带Form参数
	 * @return
	 */
	public static RequestBody buildRequestBody(File[] files, Map<String, Object> formParams) {
		if(files == null) {
			throw new IllegalArgumentException("files不能为空");
		}
		
		MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

		if(formParams != null) {
			for (String key : formParams.keySet()) {
				builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
						buildRequestBodyNoneMediaType(formParams.get(key).toString()));
			}
		}
		
		if (files != null) {
		    RequestBody fileBody = null;
		    for (int i = 0; i < files.length; i++) {
		        File file = files[i];
		        String fileName = file.getName();
		        String key = fileName;
		        int idx = key.indexOf(".");
		        if(idx > 0) {
		        	key = key.substring(0, idx);
		        }
		        fileBody = RequestBody.create(MediaType.parse(getMimeType(fileName)), file);
		        //根据文件名设置contentType
		        builder.addPart(Headers.of("Content-Disposition",
		                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
		                fileBody);
		    }
		}
		
		return builder.build();
	}
	
	/**
	 * 根据文件名获取文件的MIME类型
	 * @param fileName
	 * @return
	 */
	public static String getMimeType(String fileName) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(fileName);
        if (mimeType == null) {
        	mimeType = "application/octet-stream";
        }
        return mimeType;
	}
	
	/**
	 * 根据请求获取当前请求的缓存控制器
	 * @param request
	 * @return
	 */
	public static <T> CacheControl getCacheControl(TigerRequest<T> request) {
		CacheControl cacheControl = null;
		switch(request.getCacheStrategy()) {
			case FORCE_NETWORK:
				cacheControl = CacheControl.FORCE_NETWORK;
				break;
			case FORCE_CACHE:
				cacheControl = CacheControl.FORCE_CACHE;
				break;
			default :
				cacheControl = new CacheControl.Builder().build();
				break;
		}
		return cacheControl;
	}
	
	/*--------------------------------------------------*/
	/**
	 * 构造OkHttp中的Get请求对象
	 * @param request
	 * @return
	 */
	public static <T> Request buildGetRequest(TigerRequest<T> request) {
		return buildGetRequest(request.getUrl(), request.getParams(), request.getHeaders()).cacheControl(getCacheControl(request)).build();
	}
	
	/**
	 * 构造OkHttp中的普通Post请求对象(非文件上传)
	 * @param request
	 * @return
	 */
	public static <T> Request buildPostRequest(TigerRequest<T> request) {
		if(request instanceof TigerUploadRequest) {
			return buildPostRequest((TigerUploadRequest<T>)request);
		}
		return buildPostRequest(request.getUrl(), request.getParams(), request.getHeaders()).cacheControl(getCacheControl(request)).build();
	}
	
	/**
	 * 构造OkHttp中的文件上传Post请求
	 * @param request
	 * @return
	 */
	public static <T> Request buildPostRequest(TigerUploadRequest<T> request) {
		RequestBody body = OKHttpBuilder.buildRequestBody(request.getFiles(), request.getParams());
		return OKHttpBuilder.buildPostRequest(request.getUrl(), new TigerRequestBody<T>(body, request.getRequestCallback()), request.getHeaders()).cacheControl(getCacheControl(request)).build();
	}
	
	/*---------------------- 非常用构造 ----------------------------*/
	

	/**
	 * 根据文件路径构造一个请求体
	 * @param filePath	文件绝对路径
	 * @return
	 */
	public static RequestBody buildFileRequestBody(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			throw new IllegalArgumentException("文件" + filePath + "不存在");
		}
		return buildRequestBody(file);
	}
	
	/**
	 * 根据文件构造一个请求体
	 * @param file
	 * @return
	 */
	public static RequestBody buildRequestBodyStream(File file) {
		return RequestBody.create(MEDIA_TYPE_STREAM, file);
	}
	
	/**
	 * 根据文本内容构造一个请求体
	 * @param bodyString	文本内容，直接作为请求体
	 * @return
	 */
	public static RequestBody buildRequestBody(String bodyString) {
		return RequestBody.create(MEDIA_TYPE_STRING, bodyString);
	}
	
	/**
	 * 根据文本内容构造一个无媒体类型的请求体
	 * @param bodyString	文本内容，直接作为请求体
	 * @return
	 */
	public static RequestBody buildRequestBodyNoneMediaType(String bodyString) {
		return RequestBody.create(null, bodyString);
	}
	
	/**
	 * 根据字节数组构造一个请求体
	 * @param bodyBytes		字节数组，直接作为请求体
	 * @return
	 */
	public static RequestBody buildRequestBody(byte[] bodyBytes) {
		return RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
	}
	

	/**
	 * 构造单个文件上传的请求体(无其他附带form参数)
	 * @param file		上传的文件
	 * @return
	 */
	public static RequestBody buildRequestBody(File file) {
		return buildRequestBody(new File[]{file}, null);
	}
	
	/**
	 * 构造单个文件以及其他Form参数进行文件上传的请求体
	 * @param file			要上传的文件
	 * @param formParams	上传文件时的其他Form参数
	 * @return
	 */
	public static RequestBody buildRequestBody(File file, Map<String, Object> formParams) {
		return buildRequestBody(new File[]{file}, formParams);
	}
}
