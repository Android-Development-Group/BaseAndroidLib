package com.mobisoft.mbstest.splashScreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobisoft.mbstest.Base.BaseUrlConfig;
import com.mobisoft.mbstest.R;
import com.mobisoft.mbstest.index.IndexActivity;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebActivity;
import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.squareup.picasso.Picasso;


/**
 * Author：Created by fan.xd on 2017/3/2.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class SplashFragment extends Fragment implements SplashContract.View {

    private SplashPresenter mPresenter;
    /**
     * 闪屏壁纸
     */
    private ImageView iv_sp_fragment;
    private Context context;
    private ProgressDialog progressDialog;
    private Bundle bundle;

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }

    public SplashFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       bundle= getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.splash_frag, container, false);
        iv_sp_fragment = (ImageView) root.findViewById(R.id.iv_sp_fragment);
        context = getContext();
        mPresenter.getImageUrl();

        return root;

    }

    @Override
    public void lodeImage(String path) {
        Log.e("oye", path);
        Picasso.with(context)
                .load(path)
                .placeholder(R.drawable.sp002)
                .error(R.drawable.sp002)
                .centerCrop()
                .fit()
                .into(iv_sp_fragment);

    }

    @Override
    public void startAnimation() {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_sp_fragment, "alpha", 0f, 1f);
//        animator.setDuration(1000 * 3);
//        animator.start();
//        iv_sp_fragment.setVisibility(View.VISIBLE);
        mPresenter.startMainActivity();
    }

    @Override
    public void showMessage(String message) {
//        Snackbar.make(iv_sp_fragment, message, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

        ToastUtil.showShortToast(context, message);
        Log.i("showMessage", message);
    }

    @Override
    public void showProgressDialog(String message, String title, boolean isShow) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(title);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true); // 能够返回
            progressDialog.setCanceledOnTouchOutside(true); // 点击外部返回
        }
        progressDialog.setMessage(message);

        if (isShow && !progressDialog.isShowing()) {
            progressDialog.show();

        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void startActivity(int what) {
        FunctionConfig.Builder builder = new FunctionConfig.Builder();
        builder.setIsLeftIconShow(false);
        FunctionConfig functionConfig = builder.build();
        switch (what) {
            case 0://登录页
                CoreConfig coreConfig =
                        new CoreConfig.Builder(
                                context, ThemeConfig.DEFAULT, functionConfig)
                                .setURL(BaseUrlConfig.URL_LOGIN)
                                .setAccount("8100354")
                                .setNoAnimcation(false)
                                .setHideNavigation(false)
                                .build();
                HybridWebApp.init(coreConfig).startWebActivity(context, IndexActivity.class);

                break;
            case 1:// 首页
//                CoreConfig coreConfig1 =
//                        new CoreConfig.Builder(
//                                context, ThemeConfig.DEFAULT, functionConfig)
//                                .setAccount("8100458")//
//                                .setNoAnimcation(false)
//                                .setHideNavigation(false)
//                                .build();
//                HybridWebApp.init(coreConfig1).startWebActivity(context, MainActivity.class,bundle);
                CoreConfig coreConfig3 =
                        new CoreConfig.Builder(
                                context, ThemeConfig.DEFAULT, functionConfig)
                                .setNoAnimcation(false)
                                .setURL(BaseUrlConfig.URL_ME )
                                .setHideNavigation(false)
                                .build();
                HybridWebApp.init(coreConfig3).startWebActivity(context, MbsWebActivity.class, null);

                break;
            case 2:// 引导页
                CoreConfig coreConfig2 =
                        new CoreConfig.Builder(
                                context, ThemeConfig.DEFAULT, functionConfig)
                                .setAccount("8100458")//
                                .setNoAnimcation(false)
                                .setHideNavigation(false)
                                .build();
                HybridWebApp.init(coreConfig2).startWebActivity(context, IndexActivity.class);

                break;
        }
        ( (Activity)context).finish();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = (SplashPresenter) Preconditions.checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }
}
