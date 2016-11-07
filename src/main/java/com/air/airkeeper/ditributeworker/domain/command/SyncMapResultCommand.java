package com.air.airkeeper.ditributeworker.domain.command;

import com.air.airkeeper.ditributeworker.domain.MapResult;
import io.atomix.copycat.Command;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class SyncMapResultCommand implements Command {


    /**
     * 任务名称
     */
    private String taskName;

    /**
     *
     */
    private MapResult mapResult;


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
}
