package cn.ittiger.net.http.request;

import java.io.File;
import java.lang.reflect.Type;

/**
 * 文件上传请求，上传文件时的key值均为文件的文件名，去掉后缀
 * @auther: hyl
 * @time: 2015-9-10上午10:33:43
 */
public class TigerUploadRequest<T> extends TigerJsonRequest<T> {
	/**
	 * 要上传的文件
	 */
	private File[] files;
	
	/**
	 * 上传单个文件
	 * @param url			上传文件请求地址
	 * @param file			要上传的文件
	 */
	public TigerUploadRequest(String url, File file) {
		this(url, new File[]{file});
	}
	
	public TigerUploadRequest(String url, File file, Type resultType) {
		this(url, new File[]{file}, resultType);
	}
	
	/*------------------------------------ 根据文件路径上传单个个文件 ----------------------------------------*/
	
	/**
	 * 上传单个文件
	 * @param url			上传文件请求地址
	 * @param filePath		要上传的文件的绝对路径
	 */
	public TigerUploadRequest(String url, String filePath) {
		this(url, new File(filePath));
	}
	
	public TigerUploadRequest(String url, String filePath, Type resultType) {
		this(url, new File(filePath), resultType);
	}
	
	/*------------------------------------ 上传多个文件 ----------------------------------------*/
	
	/**
	 * 上传多个文件				
	 * @param url			上传文件请求地址
	 * @param fileKeys		上传文件时的key
	 * @param files			要上传的文件
	 */
	public TigerUploadRequest(String url, File[] files) {
		super(url);
		this.files = files;
	}
	
	public TigerUploadRequest(String url, File[] files, Type resultType) {
		super(url, resultType);
		this.files = files;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}
}
