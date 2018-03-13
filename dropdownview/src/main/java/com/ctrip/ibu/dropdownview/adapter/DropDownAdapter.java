package com.ctrip.ibu.dropdownview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrip.ibu.dropdownview.R;
import com.ctrip.ibu.dropdownview.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangjie on 28,二月,2018
 */

public class DropDownAdapter extends ArrayAdapter<DropDownItem> {

    private OnDeleteRecordListener deleteRecordListener;

    private Context mContext;

    private List<DropDownItem> mObjects;
    private ArrayList<DropDownItem> mOriginalValues;

    private final Object mLock = new Object();
    private Filter mFilter;

    private CharSequence currentPrefix;

    public DropDownAdapter(@NonNull Context context, @NonNull List<DropDownItem> list) {

        super(context, R.layout.view_auto_complete_text, list);
        this.mContext = context;

        mObjects = new ArrayList<>();
        mFilter = new ArrayFilter();
    }

    public void updateData(List<DropDownItem> list) {
        clear();
        synchronized (mLock) {
            if (ListUtil.isNullOrEmpty(list)) {
                list = new ArrayList<>();
            }
            mOriginalValues = new ArrayList<>(list);
            mObjects.addAll(list);
            addAll(mObjects);
        }
    }

    @Override
    public void clear() {
        super.clear();
        mObjects.clear();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_auto_complete_text, null);
        }

        DropDownItem item = getItem(position);
        TextView tvLabel = (TextView) convertView.findViewById(R.id.tvLabel);

        if (item != null) {
            if (!TextUtils.isEmpty(currentPrefix) && !TextUtils.isEmpty(item.getLabel())) {
                SpannableString spannableValue = new SpannableString(item.getLabel());
                ForegroundColorSpan span = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_2681ff));
                spannableValue.setSpan(span, currentPrefix.length(), item.getLabel().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tvLabel.setText(spannableValue);
            } else {
                tvLabel.setText(item.getLabel());
            }

            ImageView ivDelete = convertView.findViewById(R.id.ivDelete);
            ivDelete.setVisibility(item.isCanDelete() ? View.VISIBLE : View.GONE);
            ivDelete.setOnClickListener(new OnDeleteClickListener(position));
        }

        return convertView;
    }

    class OnDeleteClickListener implements View.OnClickListener {

        private int deletePosition;

        public OnDeleteClickListener(int position) {
            this.deletePosition = position;
        }

        @Override
        public void onClick(View v) {
            if (deleteRecordListener != null) {
                DropDownItem deleteItem = getItem(deletePosition);
                mObjects.remove(deleteItem);
                notifyDataSetChanged();
                deleteRecordListener.onDelete(deleteItem);
            }
        }
    }

    public interface OnDeleteRecordListener {

        void onDelete(DropDownItem item);
    }

    public void setDeleteRecordListener(OnDeleteRecordListener deleteRecordListener) {
        this.deleteRecordListener = deleteRecordListener;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();
            DropDownAdapter.this.currentPrefix = prefix;

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                final ArrayList<DropDownItem> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                final ArrayList<DropDownItem> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<DropDownItem> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final DropDownItem value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        value.setLabel(valueText);
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<DropDownItem>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public @Nullable
    DropDownItem getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(@Nullable DropDownItem item) {
        return mObjects.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }
}


