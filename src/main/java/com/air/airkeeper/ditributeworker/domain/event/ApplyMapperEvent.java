package com.air.airkeeper.ditributeworker.domain.event;

import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class ApplyMapperEvent {

    private static final String name = "ElectMapperSuccess_";

    private String taskName;

    /**
     * 选举成功标志位
     */
    private boolean electSuccess;


    public ApplyMapperEvent(String taskName) {
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

    public boolean isElectSuccess() {
        return electSuccess;
    }

    public void setElectSuccess(boolean electSuccess) {
        this.electSuccess = electSuccess;
    }
}
