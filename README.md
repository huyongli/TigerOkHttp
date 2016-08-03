# [TigerOkHttp](http://ittiger.cn/2016/01/06/%E5%9F%BA%E4%BA%8EOkHttp%E7%9A%84%E5%B0%81%E8%A3%85%E5%BA%93TigerOkHttp%E7%9A%84%E4%BD%BF%E7%94%A8/)
此框架是基于OkHttp的基础上进行封装的，其简化了OkHttp请求的写法，同时添加了一些其他的实用功能，使其更适用于具体的项目。
不熟悉OkHttp用法的同学请参考我的文章[Android中OkHttp的使用](http://ittiger.cn/2016/01/05/Android%E4%B8%ADOkHttp%E7%9A%84%E4%BD%BF%E7%94%A8/)
<br/>
此框架的具体用法请参考我的文章[基于OkHttp的封装库TigerOkHttp的使用](http://ittiger.cn/2016/01/06/%E5%9F%BA%E4%BA%8EOkHttp%E7%9A%84%E5%B0%81%E8%A3%85%E5%BA%93TigerOkHttp%E7%9A%84%E4%BD%BF%E7%94%A8/)
<br/><br/>

该框架主要支持的特性包括如下几点：

1.一般的get同步阻塞请求和异步请求

2.一般的post同步阻塞请求和异步请求

3.实现了文件上传功能（包含文件上传进度回调显示）

4.实现了大文件下载功能，只需要指定文件下载路径即可，也包含了下载进度的回调显示

5.实现了请求结果的自动解析，用户也可以根据需求扩展自定义结果解析类

6.对所有请求都支持直接将结果解析转换为JavaBean对象或对象集合

7.支持对返回结果结构的自定义，例如设置返回结果结构为：{flag：1|0，error：错误信息，result：请求结果}，结果解析的时候会按照此结构进行结果解析

8.支持取消某个请求<br/>


使用举例：
<br/>
1.异步Get请求如下<br/>

```
//根据请求URL构造请求对象，请求成功直接返回结果为Model集合
TigerJsonRequest<List<Model>> request = new TigerJsonRequest<List<Model>>(URL);
//添加三个请求参数
request.addParam("value", "异步get请求-返回List<Model>")
.addParam("isModel", true)
.addParam("isList", true)
.setRequestCallback(new RequestCallback<List<Model>>() {//设置异步请求回调
    @Override
    public void onSuccess(List<Model>result) {
        showResult(result.toString());
    }
 
    @Override
    public void onFailure(TigerHttpException e) {
        showResult(e.getMessage());
    }
});
//开始异步请求
TigerOkHttp.getAsync(request);
```

2.上传文件请求如下：<br/>
```
//根据上传请求地址和文件路径构造文件上传请求对象
TigerUploadRequest<String> request = new TigerUploadRequest<String>(url, filePath);
//设置上传回调监听 
request.setRequestCallback(
     new RequestCallback<String>() {
         @Override
         public void onPreExecute() {
             super.onPreExecute();
             //此处可以初始化显示上传进度UI
         }
 
         @Override
         public void onSuccess(String result) {
             //文件上传成功
         }
 
         @Override
         public void onLoading(long count, long current) {
             super.onLoading(count, current);
             //此处可以更新上传进度
         }
 
         @Override
         public void onFailure(TigerHttpException e) {
             //文件上传失败
         }
 
         @Override
         public void onAfterExecute() {
             super.onAfterExecute();
             //此处可以隐藏上传进度条
         }
 });
 //发起上传操作
 TigerOkHttp.postAsync(request);
```


