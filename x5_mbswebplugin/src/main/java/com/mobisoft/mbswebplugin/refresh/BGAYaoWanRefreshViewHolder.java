/**
 * Copyright 2015 bingoogolapple
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobisoft.mbswebplugin.refresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mobisoft.mbswebplugin.R;

/***
 * 蛇牌学院 药丸下拉风格的
 */
public class BGAYaoWanRefreshViewHolder extends BGARefreshViewHolder {
    private ImageView mHeaderArrowIv;

    private AnimationDrawable anim;


    private String mPullDownRefreshText = mContext.getString(R.string.pull_to_refresh);
    private String mReleaseRefreshText = mContext.getString(R.string.release_to_refresh);
    private String mRefreshingText = mContext.getString(R.string.loading);

    /**
     * @param context
     * @param isLoadingMoreEnabled 上拉加载更多是否可用
     */
    public BGAYaoWanRefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
        super(context, isLoadingMoreEnabled);
        initAnimation();
    }

    private void initAnimation() {
        Log.i("omg","initAnimation");

    }

    /**
     * 设置未满足刷新条件，提示继续往下拉的文本
     *
     * @param pullDownRefreshText
     */
    public void setPullDownRefreshText(String pullDownRefreshText) {
        mPullDownRefreshText = pullDownRefreshText;
    }

    /**
     * 设置满足刷新条件时的文本
     *
     * @param releaseRefreshText
     */
    public void setReleaseRefreshText(String releaseRefreshText) {
        mReleaseRefreshText = releaseRefreshText;
    }

    /**
     * 设置正在刷新时的文本
     *
     * @param refreshingText
     */
    public void setRefreshingText(String refreshingText) {
        mRefreshingText = refreshingText;
    }

    @Override
    public View getRefreshHeaderView() {
        if (mRefreshHeaderView == null) {
            mRefreshHeaderView = View.inflate(mContext, R.layout.view_refresh_header_yaowan, null);
            mRefreshHeaderView.setBackgroundColor(Color.TRANSPARENT);
            if (mRefreshViewBackgroundColorRes != -1) {
                mRefreshHeaderView.setBackgroundResource(mRefreshViewBackgroundColorRes);
            }
            if (mRefreshViewBackgroundDrawableRes != -1) {
                mRefreshHeaderView.setBackgroundResource(mRefreshViewBackgroundDrawableRes);
            }
            mHeaderArrowIv = (ImageView) mRefreshHeaderView.findViewById(R.id.iv_normal_refresh_header_yaowan);
            mHeaderArrowIv.setImageResource(R.drawable.bga_refresh_yaowan_refreshing);
            anim = (AnimationDrawable) mHeaderArrowIv.getDrawable();

        }
        return mRefreshHeaderView;
    }

    @Override
    public void handleScale(float scale, int moveYDistance) {
        Log.e("omg","handleScale"+"***scale:"+scale+"*****moveYDistance:"+moveYDistance);
        if(!anim.isRunning()){
            anim.start();
        }
    }

    @Override
    public void changeToIdle() {
        Log.i("omg","changeToIdle");
        if(anim.isRunning()){
            anim.stop();
        }
    }

    @Override
    public void changeToPullDown() {
        mHeaderArrowIv.setVisibility(View.VISIBLE);
        Log.i("omg","changeToPullDown");
    }

    @Override
    public void changeToReleaseRefresh() {
//        mHeaderArrowIv.setVisibility(View.VISIBLE);
        Log.i("omg","changeToReleaseRefresh");

    }

    @Override
    public void changeToRefreshing() {
        // 必须把动画清空才能隐藏成功
//        mHeaderArrowIv.clearAnimation();
//        mHeaderArrowIv.setVisibility(View.INVISIBLE);
        Log.i("omg","changeToRefreshing");


    }

    @Override
    public void onEndRefreshing() {
        mHeaderArrowIv.setVisibility(View.VISIBLE);
        anim.stop();
        Log.i("omg","onEndRefreshing");

    }

}