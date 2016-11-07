package com.air.airkeeper.ditributeworker.domain.command;

import io.atomix.copycat.Command;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class ApplyMapperCommand implements Command {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 申请地址
     */
    private String address;

    /**
     * 任务占有资源的最后时间  单位毫秒
     * ttl = now+(time to live)
     */
    private long ttl;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
