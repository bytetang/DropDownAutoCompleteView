package com.ctrip.ibu.dropdownview.util;

import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * create by jie.tang
 */
public class JsonUtil {

    public static String toJson(Object object, boolean excludeFieldsWithoutExposeAnnotation) {
        if (object == null)
            return "";
        GsonBuilder gsonBuilder = createGsonBuilder(excludeFieldsWithoutExposeAnnotation);
        return gsonBuilder.create().toJson(object);
    }

    private static GsonBuilder createGsonBuilder(boolean excludeFieldsWithoutExposeAnnotation) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (excludeFieldsWithoutExposeAnnotation) {
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        }
        gsonBuilder.disableHtmlEscaping();
        return gsonBuilder;
    }

    public static String toJson(Object object) {
        return JsonUtil.toJson(object, true);
    }

    public static <T> T fromJson(String json, Class<T> classOfT, boolean excludeFieldsWithoutExposeAnnotation) {
        if (json == null || "".equals(json))
            return null;
        GsonBuilder gsonBuilder = createGsonBuilder(excludeFieldsWithoutExposeAnnotation);
        return gsonBuilder.create().fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader reader, Class<T> classOfT, boolean excludeFieldsWithoutExposeAnnotation) {
        GsonBuilder gsonBuilder = createGsonBuilder(excludeFieldsWithoutExposeAnnotation);
        return gsonBuilder.create().fromJson(reader, classOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return JsonUtil.fromJson(json, classOfT, true);
    }

    public static <T> T fromJson(String json, Type type, boolean excludeFieldsWithoutExposeAnnotation) {
        if (json == null || "".equals(json))
            return null;
        GsonBuilder gsonBuilder = createGsonBuilder(excludeFieldsWithoutExposeAnnotation);
        return gsonBuilder.create().fromJson(json, type);
    }

    public static <T> T fromJson(Reader reader, Type type) {
        return fromJson(reader, type, true);
    }

    private static <T> T fromJson(Reader reader, Type type, boolean excludeFieldsWithoutExposeAnnotation) {
        GsonBuilder gsonBuilder = createGsonBuilder(excludeFieldsWithoutExposeAnnotation);
        return gsonBuilder.create().fromJson(reader, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return JsonUtil.fromJson(json, type, true);
    }


}
