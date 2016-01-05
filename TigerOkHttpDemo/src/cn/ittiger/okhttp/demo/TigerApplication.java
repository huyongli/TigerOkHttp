package cn.ittiger.okhttp.demo;

import cn.ittiger.net.http.TigerOkHttp;
import cn.ittiger.net.http.config.TigerHttpConfig;
import android.app.Application;

public class TigerApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		//初始化
		TigerOkHttp.init(new TigerHttpConfig(getApplicationContext()));
	}
}
