package com.ctrip.ibu.dropdownview;

import com.ctrip.ibu.dropdownview.datamanager.DropDownDataType;

import java.util.List;

/**
 * Created by tangjie on 01,三月,2018
 */

public interface IDropDown {

    /**
     * 传入数据源
     */
    void setDataSource(List<String> dataSource);

    /**
     * 设置数据类型
     */
    void setDataType(DropDownDataType dataType);

    /**
     * 保存历史记录
     */
    void saveToHistory(String text);

    /**
     * 是否开启自动保存输入记录。
     * 保存时机为控件失去焦点
     */
    void enableAutoSaveHistory(boolean isEnable);
}
