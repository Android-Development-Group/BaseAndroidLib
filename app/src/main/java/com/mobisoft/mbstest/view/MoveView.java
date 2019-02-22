package com.mobisoft.mbstest.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author：Created by fan.xd on 2019/2/12.
 * Email：fang.xd@mobisoft.com.cn
 * Description：移动的View
 */
public class MoveView extends View {
	private int downY;
	private int downX;

	public MoveView(Context context) {
		super(context);
	}

	public MoveView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public MoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int  y = (int) event.getY();
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:// 触摸按下的一瞬间
				 downY = x;
				 downX = y;
				break;
			case MotionEvent.ACTION_UP:// 抬起
				float moveX = x-downX;
				float moveY = y-downY;
				layout(getLeft()+downX,getTop()+y,getRight()+downX,getBottom()+y);
				break;
			case MotionEvent.ACTION_MOVE:// 移动

				break;
			case MotionEvent.ACTION_CANCEL:// 移动到另一个View层面上
				break;
		}
		return true;
	}
}
