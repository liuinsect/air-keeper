package com.air.airkeeper.ditributeworker.domain;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class TaskEntry {

    /**
     * 任务名称
     */
    private String taskName;

    private TaskStates states;

    private String mapperAddress;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskStates getStates() {
        return states;
    }

    public void setStates(TaskStates states) {
        this.states = states;
    }

    public String getMapperAddress() {
        return mapperAddress;
    }

    public void setMapperAddress(String mapperAddress) {
        this.mapperAddress = mapperAddress;
    }

    @Override
    public String toString() {
        return "TaskEntry{" +
                "taskName='" + taskName + '\'' +
                ", states=" + states +
                ", mapperAddress='" + mapperAddress + '\'' +
                '}';
    }
}
