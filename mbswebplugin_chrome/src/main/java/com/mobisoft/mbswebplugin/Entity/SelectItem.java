package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2017/8/7.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */


/**
 * Auto-generated: 2017-08-07 16:54:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SelectItem {

    private String line;
    private String name;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLine(String line) {
        this.line = line;
    }
    public String getLine() {
        return line;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}