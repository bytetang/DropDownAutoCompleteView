package com.ctrip.ibu.dropdownview.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ctrip.ibu.dropdownview.DropDownEditTextView;
import com.ctrip.ibu.dropdownview.datamanager.DropDownDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangjie on 28,二月,2018
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DropDownEditTextView dropdownView = findViewById(R.id.dropdownView);

        final List<String> emailPreffixList = new ArrayList<>();
        emailPreffixList.add("@gmail.com");
        emailPreffixList.add("@163.com");
        emailPreffixList.add("@qq.com");
        dropdownView.setDataSource(emailPreffixList);
        dropdownView.setDataType(DropDownDataType.EMAIL);


        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownView.saveToHistory(dropdownView.getText().toString());
            }
        });
    }
}
