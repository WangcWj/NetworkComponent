package cn.wang.network.builder.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.wang.network.builder.bean.SongBean;
import cn.wang.network.builder.ui.mvp.presenter.BaseMvpPresenter;
import cn.wang.network.builder.ui.mvp.view.BaseMvpView;
import cn.wang.network.R;
import cn.wang.network.builder.bean.WeatherBean;

public class MainActivity extends BaseActivity implements BaseMvpView {

    private TextView jsonText;

    private ImageView imageView;
    private BaseMvpPresenter mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        jsonText = findViewById(R.id.jsonText);
        imageView = findViewById(R.id.imageview);
        jsonText.setText("第一个的哈哈看理解");
        mPresenter.getData();
        findViewById(R.id.uselog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonText.setText("ceshi");
                mPresenter.getData();
            }
        });
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getData();
            }
        });
        findViewById(R.id.nouselog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getSerachData();
            }
        });
    }

    @Override
    public LifecycleObserver getLifecycleObserver() {
        mPresenter = new BaseMvpPresenter();
        mPresenter.setView(this);
        return mPresenter;
    }

    @Override
    public void setData(WeatherBean bean) {
        String beanCity = bean.getCity();
        jsonText.setText(beanCity);
    }

    @Override
    public void setSearchData(SongBean beans) {
        if(null != beans) {
            List<SongBean.ResultBean> result = beans.getResult();
            if(null != result && result.size() > 0){
                SongBean.ResultBean resultBean = result.get(0);
                String thumbnail = resultBean.getThumbnail();
                jsonText.setText(thumbnail);
            }
        }
    }
}
