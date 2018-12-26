package com.mobisoft.mbswebplugin.Entity;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Author：Created by fan.xd on 2017/3/25.
 * Email：fang.xd@mobisoft.com.cn
 * Description：js返回数据标准格式
 */
public class JsResult {

    private Long id;
    /***
     * 工号
     */
    String account;
    /**
     * 存储key
     */
    String key;
    /**
     * 存储数据
     */
    String value;
    /**
     * 回掉值
     */
    boolean result;

    @Generated(hash = 869359391)
    public JsResult(Long id, String account, String key, String value,
            boolean result) {
        this.id = id;
        this.account = account;
        this.key = key;
        this.value = value;
        this.result = result;
    }

    @Generated(hash = 66829817)
    public JsResult() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getResult() {
        return this.result;
    }
}
