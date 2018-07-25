package cn.wang.network.builder.net.request;

import java.util.Map;
import cn.wang.network.builder.net.NetControl;
import cn.wang.network.builder.net.base.BaseObserver;
import cn.wang.network.builder.net.base.NetAddDestroyDisposable;
import io.reactivex.Observable;

/**
 * Created by WANG on 2018/5/4.
 * 请求从这里发起.
 * 1.这里管理基础参数
 * 2.管理Header的添加
 * 3.管理文件上传RequestBody
 */

public class NetRequest extends BaseRequest {

    public NetRequest(NetControl netControl, NetAddDestroyDisposable mDestroyDisposable) {
        super(netControl,mDestroyDisposable);
    }

    public NetRequest addParams(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    public NetRequest addParams(Map params) {
        mParams.putAll(params);
        return this;
    }

    public NetRequest baseUrl(String baseUrl) {
        netControl.getNetRetrofit().transform(baseUrl);
        return this;
    }

    public <T> NetRequest apiServer(Class<T> tClass) {
        setApiServer(tClass);
        return this;
    }

    public NetRequest needPrintLog(boolean need) {
        netControl.needPrintLog(need);
        return this;
    }

    public void execute(NetCallBack callback) {
        Observable observable = callback.getMethod(netControl.getApiServer(mClass), mParams);
        BaseObserver baseObserver = netControl.getBaseObserve(callback, mDestroyDisposable);
        subscribe(observable, baseObserver);
    }

    private NetRequest subscribe(Observable observable, BaseObserver callback) {
        netControl.subscribe(observable, callback);
        return this;
    }


}
