package com.mobisoft.mbswebplugin.view.serach;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 搜搜
 */
public interface SearchListener {
		void onClick(View v);
		void onBack(View v);
		void onCancel(View v);

		void onEditorAction(TextView v, int actionId, KeyEvent event,String editable);
		/**
		 * This method is called to notify you that, within <code>s</code>,
		 * the <code>count</code> characters beginning at <code>start</code>
		 * are about to be replaced by new text with length <code>after</code>.
		 * It is an error to attempt to make changes to <code>s</code> from
		 * this callback.
		 */
		public void beforeTextChanged(CharSequence s, int start,
									  int count, int after);
		/**
		 * This method is called to notify you that, within <code>s</code>,
		 * the <code>count</code> characters beginning at <code>start</code>
		 * have just replaced old text that had length <code>before</code>.
		 * It is an error to attempt to make changes to <code>s</code> from
		 * this callback.
		 */
		 void onTextChanged(CharSequence s, int start, int before, int count);

		 void afterTextChanged(Editable s);
	}
