package com.mobisoft.mbstest.splashScreen;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mobisoft.mbstest.data.TasksDataSource;
import com.mobisoft.mbstest.data.TasksRepository;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.mobisoft.mbswebplugin.proxy.Work.DownloadSrcCallback;
import com.mobisoft.mbswebplugin.utils.Utils;


/**
 * Author：Created by fan.xd on 2017/3/2.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class SplashPresenter implements SplashContract.Presenter, DownloadSrcCallback {

    private  SplashContract.View mPresenterView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://登录页
                    mPresenterView.startActivity(0);
                    break;
                case 1:// 首页
                    mPresenterView.startActivity(1);
                    break;
                case 2:// 引导页
                    mPresenterView.startActivity(2);
                    break;
            }
        }
    };
    TasksRepository repository;
    private boolean isDownLoadFinish;

    public SplashPresenter(TasksRepository repository, SplashContract.View view,Handler handler) {
        this.mPresenterView = Preconditions.checkNotNull(view);
        this.mPresenterView.setPresenter(this);
        this.repository = repository;
        ProxyConfig.getConfig().setDownloadSrcCallback(this);
//        this.handler = handler;
    }

    @Override
    public void getImageUrl() {
        // TODO 获取数据
        repository.getSplashImage(new TasksDataSource.LoadCallback() {
            @Override
            public void onTasksLoaded(String data) {
                mPresenterView.lodeImage(data);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * 进入首页
     */
    @Override
    public void startMainActivity() {
        repository.isFirstInstall(new TasksDataSource.LoadCallback() {
            @Override
            public void onTasksLoaded(String data) {
                getCurrentAccount();
            }

            @Override
            public void onDataNotAvailable() {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = "引导页";
                Preconditions.checkNotNull(handler).sendMessageDelayed(msg, 1000 * 2);

            }
        });
    }

    /**
     * 获取本地 存储的工号
     * 若已经存在，无需登陆直接进入app首页
     * else
     * 进入登陆页面
     */
    private void getCurrentAccount() {
         final Message msg = new Message();
          msg.what = 1;
//        msg.obj = "首页";
//        Preconditions.checkNotNull(handler).sendMessageDelayed(msg, 1000 * 2);

        repository.getCurrentAccount(new TasksDataSource.LoadCallback() {
            @Override
            public void onTasksLoaded(String data) {
                msg.what = 1;
                msg.obj = "首页";
                Preconditions.checkNotNull(handler).sendMessageDelayed(msg, 1000 * 2);
            }

            @Override
            public void onDataNotAvailable() {
                msg.what = 0;
                msg.obj = "登录";
                Preconditions.checkNotNull(handler).sendMessageDelayed(msg, 1000 * 2);
            }
        });
    }

    @Override
    public void startDownManifest() {
        Log.e("oye", "startDownManifest");
//        ProxyBuilder.create().downCache();
        ProxyConfig.getConfig().excuet();
    }

    @Override
    public void onDestroy() {
        ProxyConfig.getConfig().setDownloadSrcCallback(null);
        handler = null;
    }


    @Override
    public void start() {
//        mPresenterView.startAnimation();

    }

    @Override
    public void downLoadFinish() {
        if (!isDownLoadFinish) {
            isDownLoadFinish = true;
            mPresenterView.showMessage("资源文件更新完毕");
            mPresenterView.startAnimation();
        }


    }

    @Override
    public void downLoadStart() {
        mPresenterView.showMessage("开始更新资源文件");
//        mPresenterView.showProgressDialog("开始更新资源文件","提示",true);
    }

    @Override
    public void downLoadFailure(final String s) {

        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
//                mPresenterView.showMessage("资源文件更新失败:"+s);
                mPresenterView.startAnimation();
//                mPresenterView.showProgressDialog("资源文件更新失败:"+s,"提示",false);

            }
        });
    }

    @Override
    public void noNeedUpData() {
        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mPresenterView.showMessage("noNeedUpData");
                mPresenterView.startAnimation();

            }
        });
    }
}
