package com.air.airkeeper.ditributeworker.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 自己实现StringUtils
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-2-21
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    public static final String EMPTY = "";

    public static final String COMMA = ",";

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    public static boolean isNumeric(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        final int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param str
     * @return
     */
    public static List<String> commaToList(String str) {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isNotBlank(str)) {
            String[] array = str.split(COMMA);
            for (int i = 0; i < array.length; i++) {
                result.add(array[i]);
            }
        }
        return result;
    }

    public static List toList(String str,String regex) {
        List result = new ArrayList();
        if (StringUtils.isNotBlank(str)) {
            String[] array = str.split(regex);
            for (int i = 0; i < array.length; i++) {
                result.add(array[i]);
            }
        }
        return result;
    }


    public static String join(Collection collection, String separator) {
        return join(collection.iterator(),separator);
    }

    public static String join(Iterator iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return (String) first;
        }

        // two or more elements
        StringBuilder sb = new StringBuilder(50); // Java default is 16, probably too small
        if (first != null) {
            sb.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                sb.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                sb.append(obj);
            }
        }
        return sb.toString();
    }


    public static String format(String string, Object... args) {
        return String.format(string, args);
    }


    public static int length(String str){
        return str==null?0:str.length();
    }

    public static String getDomainByMail(String mail){
        if(isBlank(mail)){
            return null;
        }
        String domain = mail.substring(mail.indexOf("@") + 1);
        return domain;
    }

}
