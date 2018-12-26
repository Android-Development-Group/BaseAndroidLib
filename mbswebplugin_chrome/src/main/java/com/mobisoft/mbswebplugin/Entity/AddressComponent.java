package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2017/6/21.
 * Email：fang.xd@mobisoft.com.cn
 * Description：定位返回数据
 */

public class AddressComponent {
    private String country;

    private String province;
    private String district;
    private String citycode;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
