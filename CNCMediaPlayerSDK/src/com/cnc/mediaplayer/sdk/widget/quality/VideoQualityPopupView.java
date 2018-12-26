package com.cnc.mediaplayer.sdk.widget.quality;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.mediaplayer.sdk.utils.UIUtils;

import java.util.List;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.widget.quality
 * @类名 ：VideoQualityPopupView
 * @时间 ：2018/02/05 10:55
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：播放器的视频剧情弹出窗口
 */
public class VideoQualityPopupView {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private ListView mListView;

    private QualityAdapter mAdapter;
    private List<MediaPlayerVideoModule> mData;
    private Callback mCallback;

    private boolean isShowing = false;
    private MediaPlayerVideoModule mCurrentSeletedQuality;

    public VideoQualityPopupView(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View root = inflater.inflate(UIUtils.getLayoutResIDByName(mContext, "cnc_video_quality_popup_view"), null);

        mListView = (ListView) root.findViewById(UIUtils.getIdResIDByName(mContext, "quality_list_view"));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mCallback != null) {
                    if (mData != null && mData.size() > 0) {
                        MediaPlayerVideoModule quality = mData.get(position);
                        if (quality != null) {
                            mCallback.onQualitySelected(quality);
                        }
                    }
                }
            }
        });

        mAdapter = new QualityAdapter();
        mListView.setAdapter(mAdapter);

        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_OUTSIDE;
            }
        });

        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                isShowing = false;
                if (mCallback != null)
                    mCallback.onPopupViewDismiss();
            }
        });
        mPopupWindow.setContentView(root);

    }

    /**
     * 显示剧情信息
     */
    public void show(View anchor, List<MediaPlayerVideoModule> qualityList, MediaPlayerVideoModule curQuality, int x, int y, int width, int height) {

        this.mData = qualityList;
        this.mCurrentSeletedQuality = curQuality;
        mAdapter.notifyDataSetChanged();
        mPopupWindow.setWidth(width);
        mPopupWindow.setHeight(height);
        mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
        isShowing = true;

    }

    /**
     * 隐藏剧情信息
     */
    public void hide() {
        mPopupWindow.dismiss();
    }

    /**
     * 查看当前是否显示剧情信息
     * @return
     */
    public boolean isShowing() {
        return isShowing;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public interface Callback {
        void onQualitySelected(MediaPlayerVideoModule quality);

        void onPopupViewDismiss();
    }

    /**
     * 剧情信息适配器
     */
    class QualityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mData != null)
                return mData.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = new QualityItemView(mContext);
            }

            QualityItemView itemView = (QualityItemView) convertView;

            MediaPlayerVideoModule quality = mData.get(position);
            itemView.initData(quality);

            return itemView;

        }

    }

    class QualityItemView extends RelativeLayout {

        private TextView mQualityTextView;

        public QualityItemView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(context);
        }

        public QualityItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public QualityItemView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            inflate(context, UIUtils.getLayoutResIDByName(mContext, "cnc_video_quality_item_view"), this);
            mQualityTextView = (TextView) findViewById(UIUtils.getIdResIDByName(mContext, "quality_text_view"));

        }

        public void initData(MediaPlayerVideoModule quality) {
            mQualityTextView.setText(quality.getName());
            if (null != mCurrentSeletedQuality && quality.getName().equals(mCurrentSeletedQuality.getName())) {
                mQualityTextView.setTextColor(0xff00a0e9);
            } else {
                mQualityTextView.setTextColor(Color.WHITE);
            }
        }

    }

}
