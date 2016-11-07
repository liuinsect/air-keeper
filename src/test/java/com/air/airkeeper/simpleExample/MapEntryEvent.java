package com.air.airkeeper.simpleExample;

import java.io.Serializable;

/**
 * @author liukunyang
 * @date 2016/5/28.
 */
public class MapEntryEvent implements Serializable {


    private Object key;

    private Object oldValue;

    private Object value;

    public MapEntryEvent(Object key, Object oldValue, Object value) {
        this.key = key;
        this.oldValue = oldValue;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
