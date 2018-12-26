package com.mobisoft.mbswebplugin.view.PopuMenu;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Author：Created by fan.xd on 2018/6/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public class FixPopupWindow extends PopupWindow {
	public FixPopupWindow(Context context) {
		super(context);
	}

	public FixPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FixPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FixPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public FixPopupWindow() {
	}

	public FixPopupWindow(View contentView) {
		super(contentView);
	}

	public FixPopupWindow(int width, int height) {
		super(width, height);
	}

	public FixPopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
	}

	public FixPopupWindow(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
	}

	@Override
	public void showAsDropDown(View anchor) {
		if(Build.VERSION.SDK_INT == 24) {
			Rect rect = new Rect();
			anchor.getGlobalVisibleRect(rect);
			int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
			setHeight(h);
		}
		super.showAsDropDown(anchor);
	}
}
