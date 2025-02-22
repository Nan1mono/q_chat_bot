package com.project.bot.common.util.qq;

import com.alibaba.fastjson2.JSONObject;

public class QUtils {

    public static final String RAW_MESSAGE = "raw_message";

    public static final String SELF_ID = "self_id";

    public static final String GROUP_ID = "group_id";

    public static final String AT_MESSAGE = "[CQ:at,qq=%s]";

    private QUtils() {
    }

    public static String getGroupId(JSONObject jsonObject) {
        return jsonObject.getString(GROUP_ID);
    }

    public static String getQid(JSONObject jsonObject) {
        return jsonObject.getString(SELF_ID);
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
        String string;
        try {
            string = jsonObject.getString(RAW_MESSAGE);
        } catch (Exception e) {
            string = "";
        }
        return string.startsWith(String.format(AT_MESSAGE, getQid(jsonObject)));
    }

}
