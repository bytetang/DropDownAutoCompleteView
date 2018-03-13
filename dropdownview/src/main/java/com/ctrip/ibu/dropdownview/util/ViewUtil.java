package com.ctrip.ibu.dropdownview.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ViewUtil
 * Create by jie.tang
 */
public class ViewUtil {

    private ViewUtil() {

    }

    public static void setViewHidden(View view, boolean hidden) {
        if (view == null)
            return;
        view.setVisibility(hidden ? View.GONE : View.VISIBLE);
    }

    public static void setViewInvisible(View view, boolean invisible) {
        if (view == null)
            return;
        view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) getRawSize(context, TypedValue.COMPLEX_UNIT_DIP, dpValue);
    }

    public static int sp2px(Context context, float dpValue) {
        return (int) getRawSize(context, TypedValue.COMPLEX_UNIT_SP, dpValue);
    }

    public static float getDimension(Context context, int dimen) {
        return context.getResources().getDimension(dimen);
    }

    //获取当前分辨率下指定单位对应的像素大小
    public static float getRawSize(Context context, int unit, float size) {
        return TypedValue.applyDimension(unit, size, context.getResources().getDisplayMetrics());
    }
}
