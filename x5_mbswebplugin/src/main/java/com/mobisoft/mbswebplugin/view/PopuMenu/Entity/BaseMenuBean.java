package com.mobisoft.mbswebplugin.view.PopuMenu.Entity;

/**
 * Created by zz on 2016/8/19.
 */
public class BaseMenuBean {

    private boolean isSelected;
    

	public boolean isSelected() {
		return (isSelected!=true?false:true);
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

    
}
