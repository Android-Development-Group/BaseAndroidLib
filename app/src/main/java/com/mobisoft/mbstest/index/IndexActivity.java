package com.mobisoft.mbstest.index;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mobisoft.mbstest.Base.BaseUrlConfig;
import com.mobisoft.mbstest.Injection;
import com.mobisoft.mbstest.R;
import com.mobisoft.mbstest.UMTest;
import com.mobisoft.mbstest.data.TasksDataSource;
import com.mobisoft.mbstest.data.TasksRepository;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebActivity;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebFragment;
import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;
import com.mobisoft.mbswebplugin.utils.FileUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.view.progress.CustomDialog;

public class IndexActivity extends AppCompatActivity {

    private EditText input;
    private TasksRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        input = (EditText) findViewById(R.id.checkedTextView);
        repository = Injection.provideTasksRepository(IndexActivity.this);

        String  deviceInfo= UMTest.getDeviceInfo(this);
        Log.i("deviceInfo",deviceInfo);
        input.setText(deviceInfo);
    }

    /**
     * @param view
     */
    public void btnOnClick(View view) {
        FunctionConfig.Builder builder = new FunctionConfig.Builder();
        builder.setIsLeftIconShow(false);
        FunctionConfig functionConfig = builder.build();
        switch (view.getId()) {
            case R.id.button:// 自定义主页
                CoreConfig coreConfig1 =
                        new CoreConfig.Builder(
                                IndexActivity.this, ThemeConfig.DEFAULT, functionConfig)
                                .setAccount("8100458")//
                                .setNoAnimcation(false)
                                .setHideNavigation(false)
                                .build();
                HybridWebApp.init(coreConfig1).startWebActivity(IndexActivity.this, MainActivity.class, null);

                break;
            case R.id.button2:// 默认页面
                Bundle bundle = new Bundle();
                bundle.putBoolean(MbsWebFragment.IS_REFRESH_ENABLE,true);
                CoreConfig coreConfig2 =
                        new CoreConfig.Builder(
                                IndexActivity.this, ThemeConfig.DEFAULT, functionConfig)
                                .setNoAnimcation(false)
                                .setURL(BaseUrlConfig.URL_INDEX)
                                .setHideNavigation(false)
                                .build();
                HybridWebApp.init(coreConfig2).startWebActivity(IndexActivity.this, MbsWebActivity.class, bundle);

                break;
            case R.id.button3:// 登录
                String s = String.valueOf(input.getText());
                if (!TextUtils.isEmpty(s)) {
                    repository.login(new TasksDataSource.LoadCallback() {
                        @Override
                        public void onTasksLoaded(String data) {

                        }

                        @Override
                        public void onDataNotAvailable() {

                        }
                    }, s);

                    CoreConfig coreConfig3 =
                            new CoreConfig.Builder(
                                    IndexActivity.this, ThemeConfig.DEFAULT, functionConfig)
                                    .setAccount("8100458")//
                                    .setNoAnimcation(false)
                                    .setHideNavigation(false)
                                    .build();
                    HybridWebApp.init(coreConfig3).startWebActivity(IndexActivity.this, MainActivity.class, null);
                    IndexActivity.this.finish();
                }

                break;
            case R.id.button4:// 退出
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("提示");
                builder1.setMessage("退出登录？");
                builder1.setNegativeButton("取消", null);
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repository.removeCurrentAccount(new TasksDataSource.LoadCallback() {
                            @Override
                            public void onTasksLoaded(String data) {
                                ToastUtil.showShortToast(IndexActivity.this, data);
                            }

                            @Override
                            public void onDataNotAvailable() {

                            }
                        });
                    }
                });
                builder1.show();

                break;
            case R.id.btn_clearcache://清理缓存
                FileUtils.deleteFile(IndexActivity.this.getCacheDir());

                CustomDialog dialog = new CustomDialog(IndexActivity.this,R.style.CustomDialog);

              //  CustomDialog dialog = new CustomDialog(IndexActivity.this);
                dialog.setMessage("ssss");
                dialog.show();
                break;
            case R.id.button5://清理缓存
//                startActivity(new Intent(this, WebActivity.class));
                CoreConfig coreConfig3 =
                        new CoreConfig.Builder(
                                IndexActivity.this, ThemeConfig.DEFAULT, functionConfig)
                                .setNoAnimcation(false)
                                .setURL(BaseUrlConfig.URL_ME )
                                .setHideNavigation(false)
                                .build();
                HybridWebApp.init(coreConfig3).startWebActivity(IndexActivity.this, MbsWebActivity.class, null);

                break;
            default:

        }
    }


}
