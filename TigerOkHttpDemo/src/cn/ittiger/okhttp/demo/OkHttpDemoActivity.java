package cn.ittiger.okhttp.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.ittiger.net.common.RequestCallback;
import cn.ittiger.net.http.TigerOkHttp;
import cn.ittiger.net.http.exeception.TigerHttpException;
import cn.ittiger.net.http.request.TigerFileRequest;
import cn.ittiger.net.http.request.TigerJsonRequest;
import cn.ittiger.net.http.request.TigerUploadRequest;

public class OkHttpDemoActivity extends Activity {
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private List<String> list = new ArrayList<String>();
	private static String URL = "http://ittiger.cn:8080/AndroidServerTest/TigerOkHttpServlet?";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_layout);
		listView = (ListView) findViewById(R.id.lv_main);
		
		initList();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String value = adapter.getItem(position);
				if("同步get请求-返回String".equals(value)) {
					getSyncStringRequest();
				}
				if("同步get请求-返回Model".equals(value)) {
					getSyncModelRequest();
				}
				if("同步get请求-返回List<Model>".equals(value)) {
					getSyncListModelRequest();
				}
				
				if("同步post请求-返回String".equals(value)) {
					postSyncStringRequest();
				}
				if("同步post请求-返回Model".equals(value)) {
					postSyncModelRequest();
				}
				if("同步post请求-返回List<Model>".equals(value)) {
					postSyncListModelRequest();
				}
				
				if("异步get请求-返回String".equals(value)) {
					getAsyncStringRequest();
				}
				if("异步get请求-返回Model".equals(value)) {
					getAsyncModelRequest();
				}
				if("异步get请求-返回List<Model>".equals(value)) {
					getAsyncListModelRequest();
				}
				
				if("异步post请求-返回String".equals(value)) {
					postAsyncStringRequest();
				}
				if("异步post请求-返回Model".equals(value)) {
					postAsyncModelRequest();
				}
				if("异步post请求-返回List<Model>".equals(value)) {
					postAsyncListModelRequest();
				}
				
				if("上传文件".equals(value)) {
					uploadFile();
				}
				if("下载文件".equals(value)) {
					downlaodFile();
				}
			}
		});
	}
	
	private void initList() {
		list.add("同步get请求-返回String");
		list.add("同步get请求-返回Model");
		list.add("同步get请求-返回List<Model>");
		
		list.add("同步post请求-返回String");
		list.add("同步post请求-返回Model");
		list.add("同步post请求-返回List<Model>");
		
		list.add("异步get请求-返回String");
		list.add("异步get请求-返回Model");
		list.add("异步get请求-返回List<Model>");
		
		list.add("异步post请求-返回String");
		list.add("异步post请求-返回Model");
		list.add("异步post请求-返回List<Model>");
		
		list.add("上传文件");
		list.add("下载文件");
	}
	
	/*-------------
	 * 以下所有请求中，请求参数代表的意思如下：
	 * 参数value：请求参数
	 * 参数isModel：服务端用来判断是返回Model类型结果字符串，还是直接返回普通String
	 * 参数isList：服务端用来判断是返回Model列表结果字符串，还是返回单个Model类型结果字符串
	 * 参数method：服务端用来判断是否为文件上传或者是文件下载请求
	 * ------------*/
	
	/**
	 * 同步get请求-返回String
	 * 同步请求必须设置返回类型
	 */
	public void getSyncStringRequest() {
		final TigerJsonRequest<String> request = new TigerJsonRequest<String>(URL, String.class);
		
		request.addParam("value", "同步get请求-返回String")
		.addParam("isModel", false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result;
				try {
					result = TigerOkHttp.getSync(request);
					showResult(result.toString());
				} catch (TigerHttpException e) {
					showResult(e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 同步get请求-返回Model
	 * 同步请求必须设置返回类型
	 */
	public void getSyncModelRequest() {
		final TigerJsonRequest<Model> request = new TigerJsonRequest<Model>(URL, Model.class);
		
		request.addParam("value", "同步get请求-返回Model")
		.addParam("isModel", true)
		.addParam("isList", false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Model result;
				try {
					result = TigerOkHttp.getSync(request);
					showResult(result.toString());
				} catch (TigerHttpException e) {
					showResult(e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 同步get请求-返回List<Model>
	 * 同步请求必须设置返回类型
	 */
	private void getSyncListModelRequest() {
		final TigerJsonRequest<List<Model>> request = new TigerJsonRequest<List<Model>>(URL, Model.class);
		request.addParam("value", "同步get请求-返回List<Model>")
		.addParam("isModel", true)
		.addParam("isList", true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Model> result;
				try {
					result = TigerOkHttp.getSync(request);
					showResult(result.toString());
				} catch (TigerHttpException e) {
					showResult(e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 同步post请求-返回String
	 * 同步请求必须设置返回类型
	 */
	public void postSyncStringRequest() {
		final TigerJsonRequest<String> request = new TigerJsonRequest<String>(URL, String.class);
		
		request.addParam("value", "同步post请求-返回String")
		.addParam("isModel", false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result;
				try {
					result = TigerOkHttp.postSync(request);
					showResult(result.toString());
				} catch (TigerHttpException e) {
					showResult(e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 同步post请求-返回Model
	 * 同步请求必须设置返回类型
	 */
	public void postSyncModelRequest() {
		final TigerJsonRequest<Model> request = new TigerJsonRequest<Model>(URL, Model.class);
		
		request.addParam("value", "同步post请求-返回Model")
		.addParam("isModel", true)
		.addParam("isList", false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Model result;
				try {
					result = TigerOkHttp.postSync(request);
					showResult(result.toString());
				} catch (TigerHttpException e) {
					showResult(e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 同步post请求-返回List<Model>
	 * 同步请求必须设置返回类型
	 */
	private void postSyncListModelRequest() {
		final TigerJsonRequest<List<Model>> request = new TigerJsonRequest<List<Model>>(URL, Model.class);
		request.addParam("value", "同步post请求-返回List<Model>")
		.addParam("isModel", true)
		.addParam("isList", true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Model> result;
				try {
					result = TigerOkHttp.postSync(request);
					showResult(result.toString());
				} catch (TigerHttpException e) {
					showResult(e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 异步get请求-返回String
	 */
	public void getAsyncStringRequest() {
		final TigerJsonRequest<String> request = new TigerJsonRequest<String>(URL);
		request.addParam("value", "异步get请求-返回String")
		.addParam("isModel", false)
		.setRequestCallback(new RequestCallback<String>() {//设置异步请求回调
			@Override
			public void onSuccess(String result) {
				showResult(result);
			}
			
			@Override
			public void onFailure(TigerHttpException e) {
				showResult(e.getMessage());
			}
		});
		
		TigerOkHttp.getAsync(request);
	}
	
	/**
	 * 异步get请求-返回Model
	 */
	public void getAsyncModelRequest() {
		final TigerJsonRequest<Model> request = new TigerJsonRequest<Model>(URL);
		request.addParam("value", "异步get请求-返回Model")
		.addParam("isModel", true)
		.addParam("isList", false)
		.setRequestCallback(new RequestCallback<Model>() {//设置异步请求回调
			@Override
			public void onSuccess(Model result) {
				showResult(result.toString());
			}
			
			@Override
			public void onFailure(TigerHttpException e) {
				showResult(e.getMessage());
			}
		});
		
		TigerOkHttp.getAsync(request);
	}
	
	/**
	 * 异步get请求-返回List<Model>
	 */
	public void getAsyncListModelRequest() {
		final TigerJsonRequest<List<Model>> request = new TigerJsonRequest<List<Model>>(URL);
		request.addParam("value", "异步get请求-返回List<Model>")
		.addParam("isModel", true)
		.addParam("isList", true)
		.setRequestCallback(new RequestCallback<List<Model>>() {//设置异步请求回调
			@Override
			public void onSuccess(List<Model> result) {
				showResult(result.toString());
			}
			
			@Override
			public void onFailure(TigerHttpException e) {
				showResult(e.getMessage());
			}
		});
		TigerOkHttp.getAsync(request);
	}
	
	/**
	 * 异步post请求-返回String
	 */
	public void postAsyncStringRequest() {
		final TigerJsonRequest<String> request = new TigerJsonRequest<String>(URL);
		request.addParam("value", "异步post请求-返回String")
		.addParam("isModel", false)
		.setRequestCallback(new RequestCallback<String>() {//设置异步请求回调
			@Override
			public void onSuccess(String result) {
				showResult(result);
			}
			
			@Override
			public void onFailure(TigerHttpException e) {
				showResult(e.getMessage());
			}
		});
		
		TigerOkHttp.postAsync(request);
	}
	
	/**
	 * 异步post请求-返回Model
	 */
	public void postAsyncModelRequest() {
		final TigerJsonRequest<Model> request = new TigerJsonRequest<Model>(URL);
		request.addParam("value", "异步post请求-返回Model")
		.addParam("isModel", true)
		.addParam("isList", false)
		.setRequestCallback(new RequestCallback<Model>() {//设置异步请求回调
			@Override
			public void onSuccess(Model result) {
				showResult(result.toString());
			}
			
			@Override
			public void onFailure(TigerHttpException e) {
				showResult(e.getMessage());
			}
		});
		
		TigerOkHttp.postAsync(request);
	}
	
	/**
	 * 异步post请求-返回List<Model>
	 */
	public void postAsyncListModelRequest() {
		final TigerJsonRequest<List<Model>> request = new TigerJsonRequest<List<Model>>(URL, Model.class);
		request.addParam("value", "异步post请求-返回List<Model>")
		.addParam("isModel", true)
		.addParam("isList", true)
		.setRequestCallback(new RequestCallback<List<Model>>() {//设置异步请求回调
			@Override
			public void onSuccess(List<Model> result) {
				showResult(result.toString());
			}
			
			@Override
			public void onFailure(TigerHttpException e) {
				showResult(e.getMessage());
			}
		});
		TigerOkHttp.postAsync(request);
	}
	
	private static int REQUEST_CODE = 1;
	/**
	 * 上传文件
	 */
	public void uploadFile() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  
		startActivityForResult(intent, REQUEST_CODE);  
	}
	
	private ProgressDialog progressDialog;
	/**
	 * 上传指定文件，带进度条显示
	 * @param filePath
	 */
	public void uploadFile(String filePath) {
		//增加一个method参数，以便后台判断是否为上传文件请求
		String url = URL + "method=upload";
		//上传文件时，请使用TigerUploadRequest请求对象
		TigerUploadRequest<String> request = new TigerUploadRequest<String>(url, filePath);
		request.addParam("form1", "表单参数1")
		.addParam("form2", "表单参数2")
		.setRequestCallback(
			new RequestCallback<String>() {
				@Override
				public void onPreExecute() {
					super.onPreExecute();
					progressDialog = new ProgressDialog(OkHttpDemoActivity.this);
					progressDialog.setTitle("正在上传");
					progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setIndeterminate(false);
					progressDialog.setMax(100);
					progressDialog.setProgress(0);
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.show();
				}
		
				@Override
				public void onSuccess(String result) {
					showResult("文件上传成功：" + result);
				}
				
				@Override
				public void onLoading(long count, long current) {
					super.onLoading(count, current);
					progressDialog.setProgress((int)(current * 1.0 / count * 100));
				}

				@Override
				public void onFailure(TigerHttpException e) {
					showResult("文件上传失败：" + e.getMessage());
				}
				
				@Override
				public void onAfterExecute() {
					super.onAfterExecute();
					progressDialog.dismiss();
				}
			});
		TigerOkHttp.postAsync(request);
	}
	
	/**
	 * 下载文件，带进度条显示
	 */
	public void downlaodFile() {
		//下载后的文件保存路径
		String path = getRootPath() + "/download_picture.jpg";
		TigerFileRequest request = new TigerFileRequest(URL, path);
		request.addParam("method", "download")
		.setRequestCallback(new RequestCallback<File>() {
			@Override
			public void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(OkHttpDemoActivity.this);
				progressDialog.setTitle("正在下载");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setIndeterminate(false);
				progressDialog.setMax(100);
				progressDialog.setProgress(0);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}
	
			@Override
			public void onSuccess(File result) {
				showResult("下载成功，保存路径为：" + result.getAbsolutePath());
			}
			
			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
				progressDialog.setProgress((int)(current * 1.0 / count * 100));
			}

			@Override
			public void onFailure(TigerHttpException e) {
				showResult("下载失败：" + e.getMessage());
			}
			
			@Override
			public void onAfterExecute() {
				super.onAfterExecute();
				progressDialog.dismiss();
			}
		});
		TigerOkHttp.getAsync(request);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
			Uri selectImageUri = data.getData();//获得选中的图片的Uri
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectImageUri, filePathColumn, null, null, null);  
            cursor.moveToFirst();  
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            uploadFile(picturePath);
		}
	}
	
	public void showResult(final String result) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(OkHttpDemoActivity.this, result, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public static String getRootPath() {
		return isHasSDCard() ? getSDCardDir() : getMobileDir();
	}
	
	public static boolean isHasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取手机SD卡根目录路径
	 * @return
	 */
	public static String getSDCardDir() {
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	/**
	 * 获取手机自身根目录
	 * @return
	 */
	public static String getMobileDir() {
		return Environment.getDataDirectory().getAbsolutePath();
	}
}
