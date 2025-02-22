package com.project.bot.common.util.qq;

import com.alibaba.fastjson2.JSONObject;

public class QUtils {

    public static final String RAW_MESSAGE = "raw_message";

    private static final String AT_MESSAGE = "[CQ:at,qq=%s]";

    private QUtils() {
    }

    public static String getGroupId(JSONObject jsonObject) {
        return null;
    }

    public static String getQid(JSONObject jsonObject) {
        return null;
    }

    /**
     * 是否是来自群组的@消息
     *
     * @param jsonObject
     * @return boolean
     */
    public static boolean isGroupAt(JSONObject jsonObject) {
        if (jsonObject == null){
            return false;
        }
        String string = jsonObject.getString(RAW_MESSAGE);
        return string.startsWith(String.format(AT_MESSAGE, getQid(jsonObject)));
    }

}
