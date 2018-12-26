package com.mobisoft.mbswebplugin.view.PopuMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.view.PopuMenu.Adapter.ILinkage;
import com.mobisoft.mbswebplugin.view.PopuMenu.Adapter.RightMenuBaseListAdapter;
import com.mobisoft.mbswebplugin.view.PopuMenu.Entity.BaseMenuBean;

import java.util.List;





/**
 * Created by zz on 2016/8/19.
 */
public class ZzSecondaryLinkage<T extends BaseMenuBean> extends LinearLayout implements ILinkage<T> {

    private View rootView;
    private RelativeLayout customNoDataView;
    private ListView lvRight;
    public RightMenuBaseListAdapter rightAdapter;

    private List<T> list;
    public OnItemClickListener mItemClickListener;
	private Button btn_cancle;
	private Button btn_submit;
 

    public ZzSecondaryLinkage(Context context) {
        super(context);
        init(context, null);
    }

    public ZzSecondaryLinkage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZzSecondaryLinkage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rootView = LayoutInflater.from(context).inflate(R.layout.zz_secondary_linkage, null);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(rootView);

        lvRight = (ListView) rootView.findViewById(R.id.right_lv);

        setItemClickListener();
      
    }

 


    @Override
    public void setRightContentAdapter(RightMenuBaseListAdapter adapter) {
        rightAdapter = adapter;
        if (rightAdapter != null)
            lvRight.setAdapter(rightAdapter);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void updateData(List<T> list) {
        this.list = list;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void setCustomNoDataView(View view) {
        if (view != null) {
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));
            this.customNoDataView.removeAllViews();
            this.customNoDataView.addView(view);
        }
    }

    @Override
    public void setCustomNoDataViewWithLayoutId(int layoutId) {
        if (layoutId > 0) {
            View view = LayoutInflater.from(getContext()).inflate(layoutId, null);
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));
            this.customNoDataView.removeAllViews();
            this.customNoDataView.addView(view);
        }
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
    }

    private void setItemClickListener() {
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mItemClickListener != null && position < rightAdapter.getCount()) {
					mItemClickListener.onRightClick(view, position);
				}
			}
		});
    }

	@Override
	public void stopLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub
		
	}


}
