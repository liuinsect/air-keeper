package com.air.airkeeper.simpleExample;

import io.atomix.copycat.Command;

/**
 * @author liukunyang
 * @date 2016/5/28.
 */
public class PutCommand implements Command<Object> {

    private final Object key;
    private final Object value;

    public PutCommand(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object key() {
        return key;
    }

    public Object value() {
        return value;
    }

    @Override
    public CompactionMode compaction() {
        return CompactionMode.EXPIRING;
    }

}
