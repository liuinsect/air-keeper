package com.air.airkeeper.ditributeworker.constant;

import java.util.concurrent.ConcurrentHashMap;

/**
 * result code 返回码常量
 * Created by liukunyang on 14-12-10.
 */
public class ResultCodeConstant {

    public static ConcurrentHashMap<String, String> tipsMap;

    static {
        tipsMap = new ConcurrentHashMap<String, String>();
        tipsMap.put(ResultCodeConstant.UNKNOWN_ERROR, "未知失败");
        tipsMap.put(ResultCodeConstant.MAPPER_APPLY_EXPIRED, "申请mapper过期");
        tipsMap.put(ResultCodeConstant.MAPPER_APPLY_FAILED, "申请mapper失败");
    }

    /**
     * 未知失败
     */
    public static final String UNKNOWN_ERROR = "001";

    /**
     * 申请mapper过期
     */
    public static final String MAPPER_APPLY_EXPIRED = "101";

    /**
     * 申请mapper失败
     */
    public static final String MAPPER_APPLY_FAILED = "102";

}


