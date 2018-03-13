# DropDownAutoCompleteView
Android输入提示选择控件

1. 输入下拉提示
2. 方便记录输入记录并支持删除
3. 高亮提示文本

演示效果：


![Alt Text](https://github.com/tangchiech/DropDownAutoCompleteView/blob/master/gif/dropdown.gif)


使用教程：

```java
final DropDownEditTextView dropdownView = findViewById(R.id.dropdownView);
final List<String> emailPreffixList = new ArrayList<>();
emailPreffixList.add("@gmail.com");
emailPreffixList.add("@163.com");
emailPreffixList.add("@qq.com");
//设置数据源
dropdownView.setDataSource(emailPreffixList);
//设置数据类型,
dropdownView.setDataType(DropDownDataType.EMAIL);
```

具体参考demo程序。