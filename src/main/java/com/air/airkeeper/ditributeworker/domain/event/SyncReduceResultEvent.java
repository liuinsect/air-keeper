package com.air.airkeeper.ditributeworker.domain.event;

import com.air.airkeeper.ditributeworker.domain.ReduceResult;

import java.io.Serializable;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class SyncReduceResultEvent implements Serializable {

    public static final String name = "SyncReduceResult_";


    private String taskName;

    private ReduceResult reduceResult;

    public SyncReduceResultEvent(String taskName) {
        this.taskName = taskName;
    }

    public static String getEventName(String taskName) {
        return name + taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ReduceResult getReduceResult() {
        return reduceResult;
    }

    public void setReduceResult(ReduceResult reduceResult) {
        this.reduceResult = reduceResult;
    }
}
