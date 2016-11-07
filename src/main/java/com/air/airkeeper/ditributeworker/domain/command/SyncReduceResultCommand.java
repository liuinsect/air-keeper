package com.air.airkeeper.ditributeworker.domain.command;

import com.air.airkeeper.ditributeworker.domain.ReduceResult;
import io.atomix.copycat.Command;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class SyncReduceResultCommand implements Command {



    /**
     * 任务名称
     */
    private String taskName;

    /**
     *
     */
    private ReduceResult reduceResult;


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
