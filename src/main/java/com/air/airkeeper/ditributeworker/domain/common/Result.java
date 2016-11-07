package com.air.airkeeper.ditributeworker.domain.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liukunyang
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 6028636097083630372L;

    /**
     * 是否成功
     */
    protected boolean success = false;


    /**
     * 结果消息
     */
    private String message;


    /**
     * 返回码
     */
    private String resultCode;

    /**
     * 返回错误tips
     */
    private String resultTips;

    /**
     * 带是否成功的构造方法
     *
     * @param success
     */
    public Result(boolean success) {
        this.success = success;
    }


    /**
     * 默认构造方法
     */
    public Result() {
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }


    public String getResultTips() {
        return resultTips;
    }

    public void setResultTips(String resultTips) {
        this.resultTips = resultTips;
    }


    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultTips='" + resultTips + '\'' +
                '}';
    }

}

