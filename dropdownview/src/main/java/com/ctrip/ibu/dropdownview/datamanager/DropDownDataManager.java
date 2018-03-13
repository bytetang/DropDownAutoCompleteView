package com.ctrip.ibu.dropdownview.datamanager;


import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ctrip.ibu.dropdownview.adapter.DropDownItem;
import com.ctrip.ibu.dropdownview.util.JsonUtil;
import com.ctrip.ibu.dropdownview.util.ListUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tangjie on 28,二月,2018
 */

public class DropDownDataManager {

    private static final String STORE_TAG = "DROPDOWND_STORE";
    public static final String EMAIL_STORE_KEY = "key_email";
    private static DropDownDataManager instance;

    private SharedPreferences sharedPref = null;

    public static DropDownDataManager get(Context context) {
        if (instance == null) {
            instance = new DropDownDataManager(context);

        }
        return instance;
    }

    private DropDownDataManager(Context context) {
        sharedPref = context.getSharedPreferences(
                STORE_TAG, Context.MODE_PRIVATE);
    }

    public List<DropDownItem> getHistoryRecords(String key) {
        return JsonUtil.fromJson(sharedPref.getString(key, ""), new TypeToken<ArrayList<DropDownItem>>() {
        }.getType());
    }

    public void setHistoryRecords(String key, String record) {
        DropDownItem dropDownItem = new DropDownItem(record, true);
        setHistoryRecords(key, dropDownItem);
    }

    private void setHistoryRecords(String key, DropDownItem record) {
        List<DropDownItem> historyList = getHistoryRecords(key);
        if (ListUtil.isNullOrEmpty(historyList)) {
            historyList = new ArrayList<>();
        }
        historyList.add(0, record);
        removeItemListDupli(historyList);
        sharedPref.edit().putString(key, JsonUtil.toJson(historyList)).apply();
    }

    public void removeRecord(String key, String record) {
        List<DropDownItem> historyList = getHistoryRecords(key);

        List<DropDownItem> removeList = new ArrayList<>();
        if (!ListUtil.isNullOrEmpty(historyList)) {
            for (DropDownItem history : historyList) {
                if (history.getLabel().equals(record)) {
                    removeList.add(history);
                }
            }
        }

        if (!ListUtil.isNullOrEmpty(removeList)) {
            historyList.removeAll(removeList);
        }
        sharedPref.edit().putString(key, JsonUtil.toJson(historyList)).apply();
    }

    /**
     * 去重
     */
    public void removeItemListDupli(List<DropDownItem> dropList) {
        Set<DropDownItem> set = new LinkedHashSet<>();
        set.addAll(dropList);
        dropList.clear();
        dropList.addAll(set);
    }

    public List<DropDownItem> matchInHistory(String key, String record) {
        List<DropDownItem> matchedItems = new ArrayList<>();
        if (TextUtils.isEmpty(record)) {
            return matchedItems;
        }
        List<DropDownItem> historyList = getHistoryRecords(key);

        if (!ListUtil.isNullOrEmpty(historyList)) {
            for (DropDownItem history : historyList) {
                if (history.getLabel().startsWith(record)) {
                    matchedItems.add(history);
                }
            }
        }
        return matchedItems;
    }

    public List<String> getStringList(String key) {
        return JsonUtil.fromJson(sharedPref.getString(key, ""), new TypeToken<ArrayList<DropDownItem>>() {
        }.getType());
    }


    public List<DropDownItem> convertToDropDownListWithoutDL(List<String> list) {
        return convertToDropDownList(list, false);
    }

    public List<DropDownItem> convertToDropDownListWithDL(List<String> list) {
        return convertToDropDownList(list, true);
    }

    private List<DropDownItem> convertToDropDownList(List<String> list, boolean canDelete) {
        List<DropDownItem> dropDownItems = new ArrayList<>();

        if (ListUtil.isNullOrEmpty(list)) {
            return dropDownItems;
        }

        for (String s : list) {
            DropDownItem dropDownItem = new DropDownItem(s, canDelete);
            dropDownItems.add(dropDownItem);
        }
        return dropDownItems;
    }
}
