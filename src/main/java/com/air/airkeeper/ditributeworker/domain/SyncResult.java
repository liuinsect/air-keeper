package com.air.airkeeper.ditributeworker.domain;

import com.air.airkeeper.ditributeworker.domain.common.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liukunyang
 * @date 2016/6/5.
 */
public class SyncResult extends Result {

    /**
     * service返回的对象
     */
    private Map<String, Object> result = new HashMap<String, Object>();


    /**
     * 新增一个带key的返回结果
     *
     * @param key
     * @param obj
     * @return
     */
    public Object addDefaultModel(String key, Object obj) {
        return result.put(key, obj);
    }


    public Map<String, Object> getResult() {
        return result;
    }
}
