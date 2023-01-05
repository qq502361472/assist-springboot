package com.hjrpc.assistspringboot.util;

public class UpperCaseUtil {

    public static String toFirstUpperCase(String targetFieldName) {
        if (targetFieldName == null || "".equals(targetFieldName)) {
            throw new RuntimeException();
        }
        String first = targetFieldName.substring(0, 1).toUpperCase();
        String tail = targetFieldName.substring(1);
        return first + tail;
    }


    public static String getMethodString(String fieldName) {
        return "get" + toFirstUpperCase(fieldName);
    }

    public static String setMethodString(String fieldName) {
        return "set" + toFirstUpperCase(fieldName);
    }


    public static String getTargetFieldName(String targetFieldName, String name) {
        if ("".equals(targetFieldName)) {
            targetFieldName = name + "String";
        }
        return targetFieldName;
    }
}
