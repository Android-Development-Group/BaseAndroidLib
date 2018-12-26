package com.mobisoft.mbswebplugin.view.PopuMenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.utils.DisplayUtil;
import com.mobisoft.mbswebplugin.view.PopuMenu.Adapter.ILinkage;
import com.mobisoft.mbswebplugin.view.PopuMenu.Adapter.RightContentListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fan xuedong
 * @version V1.0
 * @date 2016年11月21日 上午10:07:07
 * @Description: 弹出菜单选择页面
 */
public class PopupMenu {

	private Context mContext;
	private FixPopupWindow mPopupWindow;
	private levelViewSubmitListner listener;
	private List<MeunItem> entities;
	private Gson gson;

	private ZzSecondaryLinkage zzSecondaryLinkage;
	private SubMintListener subMintListener;
	private List<String> lables = new ArrayList<String>();
	protected int leftPos = 0;// 左菜单标识 默认第一个
	protected String chepai;// 品牌类型
	protected String type;// 课程类型
	protected String times;// 时间

	private RightContentListAdapter adapter;

	/**
	 * 窗口監聽
	 *
	 * @author Fan xuedong
	 * @version V1.0
	 * @date 2016年11月10日 下午6:09:01
	 * @Description: TODO
	 */
	public interface SubMintListener {
		void onSubmint(List<String> labels, String type, String time);

		void onCancle();

		void onClick(View itemView, int position);
	}

	/**
	 * 設置監聽
	 *
	 * @param listener
	 */
	public void setSubMintListener(SubMintListener listener) {
		this.subMintListener = listener;
	}

	public PopupMenu(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		gson = new Gson();
	}

	public void setEntities(List<MeunItem> entities) {
		this.entities = entities;
	}

	/**
	 * 弹出控件
	 */
	public void onCreatPopupMenu(View v) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vPopWindow = inflater.inflate(R.layout.layout_popup_menu, null);
		zzSecondaryLinkage = (ZzSecondaryLinkage) vPopWindow.findViewById(R.id.ll_zzLinkage);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			mPopupWindow = new FixPopupWindow(vPopWindow, DisplayUtil.dip2px(mContext, 120),
					ViewGroup.LayoutParams.MATCH_PARENT, true);
		} else {
			mPopupWindow = new FixPopupWindow(vPopWindow, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, true);
		}

		adapter = new RightContentListAdapter(mContext, entities);
		zzSecondaryLinkage.setRightContentAdapter(adapter);
		zzSecondaryLinkage.setOnItemClickListener(new ILinkage.OnItemClickListener() {
			@Override
			public void onLeftClick(View itemView, int position) {

			}

			@Override
			public void onRightClick(View itemView, int position) {
				position++;
				for (int i = 0; i < entities.size(); i++) {
					if (position == i) {
						entities.get(position).setSelect(!entities.get(position).isSelect());
					} else entities.get(i).setSelect(false);
					entities.get(0).setName(entities.get(position).getName());
				}
				adapter.notifyDataSetChanged();
				mPopupWindow.dismiss();
				if (subMintListener != null && entities.get(position).isSelect()) {
					subMintListener.onClick(itemView, position);
				}
			}

			@Override
			public void onCancleBtnClick(View itemView) {

			}

			@Override
			public void onSubmitBtnClick(View itemView) {

			}
		});

		ColorDrawable cd = new ColorDrawable(0x000000);
		mPopupWindow.setBackgroundDrawable(cd);
		mPopupWindow.showAsDropDown(v);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);//这里必须设置为true才能点击区域外或者消失
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);

			}
		});
		backgroundAlpha(0.4f);

	}

	private void backgroundAlpha(float f) {

		WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
		lp.alpha = f;
		((Activity) mContext).getWindow().setAttributes(lp);
	}

}
