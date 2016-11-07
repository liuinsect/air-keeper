package com.air.airkeeper.ditributeworker.domain.event;

import com.air.airkeeper.ditributeworker.domain.MapResult;

import java.io.Serializable;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class SyncMapResultEvent implements Serializable {

    public static final String name = "SyncMapResult_";


    private String taskName;

    private MapResult mapResult;

    public SyncMapResultEvent(String taskName) {
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

    public MapResult getMapResult() {
        return mapResult;
    }

    public void setMapResult(MapResult mapResult) {
        this.mapResult = mapResult;
    }

    @Override
    public String toString() {
        return "SyncMapResultEvent{" +
                "taskName='" + taskName + '\'' +
                ", mapResult=" + mapResult +
                '}';
    }
}
