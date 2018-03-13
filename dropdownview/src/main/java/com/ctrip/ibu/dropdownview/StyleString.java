package com.ctrip.ibu.dropdownview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

/**
 * StyleString
 * Created by z_dong on 2017/6/1.
 */
public class StyleString {
    private Context mContext;
    private String mText;
    private SpannableStringBuilder mBuilder;

    public StyleString(Context context, String text) {
        mContext = context;
        mBuilder = new SpannableStringBuilder();
        mBuilder.append(mText = text);
    }

    public StyleString setForegroundColor(@ColorRes int color) {
        mBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, color)), 0,
                mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public StyleString setBackgroundColor(@ColorRes int color) {
        mBuilder.setSpan(new BackgroundColorSpan(ContextCompat.getColor(mContext, color)), 0,
                mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public StyleString setFontSizePX(int fontSizePX) {
        mBuilder.setSpan(new AbsoluteSizeSpan(fontSizePX), 0, mText.length(), Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public StyleString setFontSize(int dimenSize) {
        mBuilder.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize
                (dimenSize)), 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * About typeFace see {@link android.graphics.Typeface}
     */
    public StyleString setFontStyle(int typeFace) {
        mBuilder.setSpan(new StyleSpan(typeFace), 0, mText.length(), Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public StyleString setUnderline() {
        mBuilder.setSpan(new UnderlineSpan(), 0, mText.length(), Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public StyleString setNoUnderline() {
        mBuilder.setSpan(new NoUnderlineSpan(), 0, mText.length(), Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * If StyleString was used as content of TextView or EditText,
     * setMovementMethod should be called like below:
     * <p/>
     * <pre>
     * TextView textView = new TextView(context);
     * textView.setMovementMethod(LinkMovementMethod.getInstance());
     * </pre>
     */
    public StyleString setClickable(ClickableSpan clickable) {
        mBuilder.setSpan(clickable, 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public StyleString setUri(String uri) {
        mBuilder.setSpan(new URLSpan(uri), 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public StyleString setStrikethrough() {
        mBuilder.setSpan(new StrikethroughSpan(), 0, mText.length(), Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpannableStringBuilder toStyleString() {
        return mBuilder;
    }

    @SuppressLint("ParcelCreator")
    public class NoUnderlineSpan extends UnderlineSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }
}
