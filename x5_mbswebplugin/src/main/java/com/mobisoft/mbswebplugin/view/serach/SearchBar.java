package com.mobisoft.mbswebplugin.view.serach;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.R;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

/**
 * Author：Created by fan.xd on 2018/7/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：搜索布局
 */
public class SearchBar extends LinearLayout {

	/**
	 * 搜索头布局
	 */
	private LinearLayout ll_search;
	/**
	 * 返回按钮
	 */
	private TextView search_back;
	/**
	 * 搜索框
	 */
	private EditText search_editext;
	/**
	 * 取消按钮
	 */
	private TextView search_cancel;
	private SearchListener serachLitener;
	private TextView.OnEditorActionListener onEditorActionListener;

	public SearchBar(Context context) {
		super(context);
		onCreate(context);
	}

	public SearchBar(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		onCreate(context);
	}

	public SearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		onCreate(context);
	}

	private void onCreate(Context context) {
		LayoutInflater.from(context).inflate(R.layout.search_title2, this, true);
		//nike
		ll_search = (LinearLayout) findViewById(R.id.search_ll);
		search_back = (TextView) findViewById(R.id.search_back);
		search_editext = (EditText) findViewById(R.id.search_editext);
		search_cancel = (TextView) findViewById(R.id.search_cancel);
		ll_search.setVisibility(VISIBLE);
		search_editext.setImeOptions(IME_ACTION_SEARCH);
		initEvent();
	}

	private void initEvent() {
		search_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (serachLitener != null) {
					serachLitener.onBack(v);
					serachLitener.onClick(v);
				}

			}
		});
		search_editext.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if (serachLitener != null)
					serachLitener.beforeTextChanged(s, start, count, after);

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (serachLitener != null)
					serachLitener.onTextChanged(s, start, before, count);
				if (!TextUtils.isEmpty(search_editext.getText())) {
					search_cancel.setVisibility(VISIBLE);
				} else {
					search_cancel.setVisibility(INVISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (serachLitener != null)
					serachLitener.afterTextChanged(s);
			}
		});
		search_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (serachLitener != null) {
					serachLitener.onClick(v);
					serachLitener.onCancel(v);
				}
				search_editext.setText("");
			}
		});
		search_editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (serachLitener != null) {
					if (!TextUtils.isEmpty(search_editext.getText())) {
						serachLitener.onEditorAction(v, actionId, event, search_editext.getText().toString());
					} else {
						serachLitener.onEditorAction(v, actionId, event, "");

					}
				}
				return false;
			}
		});
	}


	public void setPlaceholder(String placeholder) {
		search_editext.setHint(placeholder);
	}


	public void setSerachLitener(SearchListener serachLitener) {
		this.serachLitener = serachLitener;
	}
}
