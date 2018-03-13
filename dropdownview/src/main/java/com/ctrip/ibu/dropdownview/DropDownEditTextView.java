package com.ctrip.ibu.dropdownview;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ctrip.ibu.dropdownview.adapter.DropDownAdapter;
import com.ctrip.ibu.dropdownview.adapter.DropDownItem;
import com.ctrip.ibu.dropdownview.datamanager.DropDownDataManager;
import com.ctrip.ibu.dropdownview.datamanager.DropDownDataType;
import com.ctrip.ibu.dropdownview.util.ListUtil;
import com.ctrip.ibu.dropdownview.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入选择控件
 * Created by tangjie on 28,二月,2018
 */

public class DropDownEditTextView extends AppCompatAutoCompleteTextView implements IDropDown, DropDownAdapter.OnDeleteRecordListener {

    private List<String> dataSource;
    private DropDownDataType dataType;

    private DropDownAdapter dropDownAdapter;
    private DropDownDataManager dataManager;

    private String emailPrefix = "";

    public DropDownEditTextView(Context context) {
        this(context, null);
    }

    public DropDownEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dataManager = DropDownDataManager.get(context);
        this.addTextChangedListener(new TextChangeListener());
        this.setOnClickListener(new AutoCompleteOnClickListener());

        //默认样式
        setThreshold(1);
        setDropDownBackgroundResource(R.drawable.bg_auto_complete_drop_down);
        setDropDownVerticalOffset(ViewUtil.dp2px(this.getContext(), 5));

        initAdapter();

    }

    @Override
    public void setDataType(DropDownDataType dataType) {
        this.dataType = dataType;
    }

    private void initAdapter() {
        if (ListUtil.isNullOrEmpty(this.dataSource)) {
            dropDownAdapter = new DropDownAdapter(this.getContext(), new ArrayList<DropDownItem>());
        } else {
            dropDownAdapter = new DropDownAdapter(this.getContext(),
                    DropDownDataManager.get(getContext()).convertToDropDownListWithoutDL(this.dataSource));
        }

        dropDownAdapter.setDeleteRecordListener(this);
        this.setAdapter(dropDownAdapter);
    }

    @Override
    public void setDataSource(List<String> dataSource) {
        this.dataSource = dataSource;
        initAdapter();
    }

    @Override
    public void saveToHistory(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        dataManager.setHistoryRecords(dataType.getKey(), text);
    }

    @Override
    public void enableAutoSaveHistory(boolean isEnable) {
        if (isEnable) {
            setOnFocusChangeListener(new AutoCompleteOnFocusChangeListener());
        } else {
            setOnFocusChangeListener(null);
        }
    }

    class AutoCompleteOnFocusChangeListener implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                //auto save in sp when focus lose
                Log.d("DropDownEditTextView", "auto save history");
                String inputText = DropDownEditTextView.this.getText().toString().trim();
                if (!TextUtils.isEmpty(inputText)) {
                    dataManager.setHistoryRecords(dataType.getKey(),
                            DropDownEditTextView.this.getText().toString().trim());
                }
            }
        }
    }

    class AutoCompleteOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof DropDownEditTextView) {
                String inputText = ((DropDownEditTextView) v).getText().toString().trim();
                //开始输入
                if (TextUtils.isEmpty(inputText)) {
                    buildInitDropDownData();
                    DropDownEditTextView.this.showDropDown();
                }
            }
        }
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                if (buildInitDropDownData()) {
                    showDropDown();
                    return;
                }
            }

            buildDropDownData(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onDelete(DropDownItem item) {
        dataManager.removeRecord(dataType.getKey(), item.getLabel().toString());
    }

    private boolean buildInitDropDownData() {
        List<DropDownItem> initList = dataManager.getHistoryRecords(DropDownEditTextView.this.dataType.getKey());
        dropDownAdapter.updateData(initList);

        return !ListUtil.isNullOrEmpty(initList);
    }

    private void buildDropDownData(String inputText) {

        if (inputText == null || inputText.trim().length() == 0) {
            dropDownAdapter.updateData(new ArrayList<DropDownItem>());
            return;
        }
        List<DropDownItem> list = new ArrayList<>();

        String prefix = "";

        //add history records
        List<DropDownItem> historyList = dataManager.getHistoryRecords(dataType.getKey());

        if (!ListUtil.isNullOrEmpty(historyList)) {
            list.addAll(dataManager.matchInHistory(dataType.getKey(), inputText.toString()));
        }

        //add sourceData records
        if (DropDownEditTextView.this.dataType.getKey().equals(DropDownDataType.EMAIL.getKey())) {

            if (ListUtil.isNullOrEmpty(this.dataSource)) {
                this.dataSource = dataManager.getStringList(DropDownDataManager.EMAIL_STORE_KEY);
            }

            if (ListUtil.isNullOrEmpty(this.dataSource)) {
                throw new RuntimeException("Email数据没有初始化或没有传入数据源");
            }

            for (String data : DropDownEditTextView.this.dataSource) {
                String stringWithPrefix = "";
                if (inputText.contains("@") && inputText.indexOf("@") != 0) {
                    prefix = inputText.substring(0, inputText.indexOf("@"));
                    emailPrefix = prefix;
                    stringWithPrefix = prefix + data;
                } else {
                    stringWithPrefix = inputText + data;
                    prefix = "";
                }

                DropDownItem dropDownItem = new DropDownItem(stringWithPrefix, false);
                list.add(dropDownItem);
            }
        } else {
            List<DropDownItem> dataSourceItems = dataManager.convertToDropDownListWithoutDL(dataSource);
            if (!ListUtil.isNullOrEmpty(dataSourceItems)) {
                list.addAll(dataManager.convertToDropDownListWithoutDL(dataSource));
            }
        }

        //邮箱前缀一致则不需要重新更新数据，解决数据重新update控件闪动的问题。
        if (!isEmailDataNeedRefresh(prefix)) {
            return;
        }
        //去重
        dataManager.removeItemListDupli(list);
        dropDownAdapter.updateData(list);
    }

    private boolean isEmailDataNeedRefresh(String prefix) {
        if (DropDownEditTextView.this.dataType.getKey().equals(DropDownDataType.EMAIL.getKey()) &&
                !TextUtils.isEmpty(prefix) && prefix.equals(emailPrefix)) {
            return false;
        }

        return true;
    }
}
