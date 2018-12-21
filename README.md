# WeNet
##### Retrofit+Rxjava组合的高性能网络框架，代码解耦，各个功能独立维护，网络请求中只维护一个Retrofit.Builder和OkhttpClient.Builder的实例。功能如下：   
#### 1.链式调用。   
#### 2.动态改变BaseUrl和ApiServer。   
#### 3.动态添加拦截器。   
#### 4.自定义异常处理，以及Resopnse结果预先处理。

#### 5.随心所欲的日志系统。让你清楚你的每次请求细节。

#### 6.每次网络请求都可随时取消，页面销毁自动取消网络请求。    

# Download   

**Version:** [![Download](https://api.bintray.com/packages/wangchaochao/WeNet/wenet/images/download.svg)](https://bintray.com/wangchaochao/WeNet/wenet/_latestVersion)

1.项目根目录的build.gradle文件里面:  

```
allprojects {
    repositories {
        google()
        maven {url 'https://dl.bintray.com/wangchaochao/WeNet'}
        jcenter()
    }
}

```

2.再使用的build.gradle文件里面添加:

```
implementation 'cn.wang.wenet:wenet:1.0.1'

```

3.如果对网络请求的生命周期进行管理的话，请在你的BaseActivity里面实现NetLifecycleControl接口，参考如下代码完成生命周期的控制。

```
public abstract class BaseActivity extends AppCompatActivity implements NetLifecycleControl {

    protected CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
        
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (null != mCompositeDisposable && null != disposable) {
            mCompositeDisposable.add(disposable);
        }
    }

    @Override
    protected void onDestroy() {
        if(null != mCompositeDisposable && !mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }
}

```
4.基本的使用方式如下:

初始化过程。

```
//再Application中或者别的地方初始化
 NetControl.init("你的baseUrl",ApplicationContext);
```

如果你的后台接口返回数据类型如下：

```
{
    "code":200,
    "message":"成功!",
    "data":{
    }
}

{
    "status":200,
    "msg":"成功!",
    "data":{
    }
}

```

使用:

```
 //注意你的bean是已经去除了 三个返回状态字段的bean,NetBaseResultBean这个类里面会帮助你去接收code，message，data的值。
 @GET("weatherApi")
 Observable<NetBaseResultBean<bean>> getCityWeather(@QueryMap Map<String, Object> params);
 
 //网络请求
 //这个this就是实现了NetLifecycleControl接口.
  NetControl.request(MainActivity.this)
                        .addParams("city", "杭州")
                        //*******注意***********
                        //使用 execute(new NetCallBack())方式
                        .execute(new NetCallBack<WeatherBean>() {
                            @Override
                            public Observable<BaseResultBean<WeatherBean>> getMethod(NetRequest request, Map<String, Object> params) {
                                return request.getApiService(ApiService.class).getCityWeather(params);
                            }

                            @Override
                            public void onSuccess(WeatherBean weatherBean) {
                               //数据返回成功
                            }

                            @Override
                            public void onError(NetException e) {
                                //数据请求失败
                            }
                        });
 
```

如果后台数据返回不一致或者很乱，那就选择原样返回模式，也就是数据不会经过逻辑判断，全部原样返回：

```
@GET("searchPoetry")
Observable<SongBean> getPoetry(@QueryMap Map<String, Object> params);

 NetControl.request(this)
                .baseUrl(BaseAPI.BASE_SINGING_URL)
                .addParams("name", "忆江南")
                //*******注意***********
                //这里使用的是executeForObject(new NetObjectCallBack())模式.
                .executeForObject(new NetObjectCallBack<SongBean>() {
                    @Override
                    public Observable<SongBean> getMethod(NetRequest request, Map<String, Object> params) {
                        return request.getApiService(ApiSong.class).getPoetry(params);
                    }

                    @Override
                    public void onSuccess(SongBean o) {
                       Log.e("WANG","FirstFragment.onSuccess."+o.toString() );
                    }

                    @Override
                    public void onError(NetException e) {

                    }
                });
```





### V1.0.0    

18-12-20 更新日志:

- 增加自定义数据返回正确时的code值。
- 完善动态添加Interceptor的功能，该功能针对每个接口，并不是全局。
- 加快初始化过程。

18-12-18 更新日志:
* 底层结构重构，提高了每个类功能的独立性。
* 改掉了动态切换BaseUrl时，BaseUrl复用的问题。将Okhttp和Retrofit单例对象的模式改为Okhttp.Builder跟Retrofit.Builder对象为单例，增强动态配置功能。
* 简化了网络请求的调用方式。   

18-8-2 更新日志:   
* 删除默认的BaseUrl，ApiServer。改为在初始化的时候设置，如果不设置的话，框架将无法使用。   
* 增加日志的全局设置，需要在初始化的时候选择是否打开日志，以及设置打印日志的TAG。该日志打印包括框架的异常报错和网络请求以及结果的日志打印。包括Intercepter的日志打印。

18-7-30 更新日志:   
* 增加请求返回结果为String类型的Convert.Factory；
* 增加异常拦截判断异常类型以及code错误码的自定义判断，当出现异常或者获取不到数据的时候将会Toast提示；
* Retrofit+Rxjava生命周期的管理。页面销毁的时候结束网络请求，也可以根据自己的需求再onStop和onResume的时候去管理网络请求。

18-7-19 更新日志:   
* 该版为基础版本，目前支持，GET，POST请求；   
* 动态替换BaseUrl，动态替换ApiServer，动态添加去除日志拦截器。  


​     


