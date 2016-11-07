package com.air.airkeeper.ditributeworker.domain.common;

import com.air.airkeeper.ditributeworker.constant.ResultCodeConstant;
import com.air.airkeeper.ditributeworker.util.StringUtils;

import java.text.MessageFormat;

/**
 * Result工厂类
 * Created by liukunyang on 15-1-15.
 */
public class ResultFactory {

    /**
     * @param resultCode
     * @return
     */
    public static Result newFailResult(String resultCode) {
        Result result = new Result();
        result.setSuccess(false);
        result.setResultCode(resultCode);
        String tips = ResultCodeConstant.tipsMap.get(resultCode);
        if (StringUtils.isNotBlank(tips)) {
            result.setResultCode(resultCode);
            result.setResultTips(tips);
        }
        return result;
    }

    /**
     * 支持格式化tips建议试用该方法
     * sample:ResultFactory.newFailResult("111","本活动只对{0}等地区的申请开放，感谢参与！","四川")
     * output:本活动只对四川等地区的申请开放，感谢参与！
     *
     * @param resultCode
     * @param tips
     * @param params
     * @return
     */
    public static Result newFailResult(String resultCode, String tips, Object... params) {
        String message = MessageFormat.format(tips, params);
        Result result = new Result();
        result.setSuccess(false);
        result.setResultCode(resultCode);
        result.setResultTips(message);
        return result;
    }


    /**
     * 返回成功的Result
     *
     * @return
     */
    public static Result newSuccessResult() {
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }


}
