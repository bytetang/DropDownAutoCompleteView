package com.ctrip.ibu.dropdownview.datamanager;

/**
 * Created by tangjie on 28,二月,2018
 */

public enum DropDownDataType {

    EMAIL("email");

    String key;

    DropDownDataType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
