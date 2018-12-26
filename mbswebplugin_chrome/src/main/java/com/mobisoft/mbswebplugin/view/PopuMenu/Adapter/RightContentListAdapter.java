package com.mobisoft.mbswebplugin.view.PopuMenu.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.utils.ViewUtil;

import java.util.List;

/**
 * Created by zz on 2016/8/20.
 */
public class RightContentListAdapter
		extends RightMenuBaseListAdapter<RightListViewHolder, MeunItem> {

	public RightContentListAdapter(Context ctx, List<MeunItem> list) {
		super(ctx, list);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size() - 1;
	}

	@Override
	public long getItemId(int i) {
		return super.getItemId(i + 1);
	}

	@Override
	public MeunItem getItem(int i) {
		return list == null ? null : list.get(i + 1);
	}

	@Override
	public RightListViewHolder getViewHolder() {
		return new RightListViewHolder();
	}

	@Override
	public void bindView(RightListViewHolder rightListViewHolder, View itemView) {
		ViewUtil.scaleContentView((ViewGroup) itemView.findViewById(R.id.ll_root));
		rightListViewHolder.ivPic = (ImageView) itemView.findViewById(R.id.iv_menu_icon);
		rightListViewHolder.tvProductName = (TextView) itemView.findViewById(R.id.tv_menu_name);
//		rightListViewHolder.item_layout = (ViewGroup) itemView.findViewById(R.id.root);
	}

	@Override
	public void bindData(RightListViewHolder rightListViewHolder, int position) {
		// Glide.with(context).load(Constants.BASE_URL +
		// getItem(position).getPicUrl()).into(rightListViewHolder.ivPic);
		rightListViewHolder.tvProductName.setText(getItem(position).getName());
		if (getItem(position).isSelect()) {
			rightListViewHolder.ivPic.setVisibility(View.VISIBLE);
			rightListViewHolder.tvProductName.setTextColor(Color.parseColor("#F49108"));
		} else {
			rightListViewHolder.tvProductName.setTextColor(Color.parseColor("#333333"));
			rightListViewHolder.ivPic.setVisibility(View.GONE);
		}

	}

	@Override
	public int getLayoutId() {
		return R.layout.list_item_content;
	}

}
