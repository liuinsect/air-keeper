package com.air.airkeeper.ditributeworker.core;

import com.air.airkeeper.ditributeworker.domain.event.SyncReduceResultEvent;
import com.air.airkeeper.ditributeworker.constant.ResultCodeConstant;
import com.air.airkeeper.ditributeworker.domain.ReduceResult;
import com.air.airkeeper.ditributeworker.domain.command.SyncReduceResultCommand;
import com.air.airkeeper.ditributeworker.domain.common.Result;
import com.air.airkeeper.ditributeworker.domain.TaskEntry;
import com.air.airkeeper.ditributeworker.domain.TaskStates;
import com.air.airkeeper.ditributeworker.domain.command.ApplyMapperCommand;
import com.air.airkeeper.ditributeworker.domain.command.SyncMapResultCommand;
import com.air.airkeeper.ditributeworker.domain.common.ResultFactory;
import com.air.airkeeper.ditributeworker.domain.event.SyncMapResultEvent;
import io.atomix.copycat.server.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class AirKeeperStateMachine extends BaseStateMachine {

    private Logger logger = LoggerFactory.getLogger(AirKeeperStateMachine.class);

    /**
     * 选举mapper
     *
     * @param applyMapperCommit
     * @return
     */
    public Result applyMapper(Commit<ApplyMapperCommand> applyMapperCommit) {
        try{
            String taskName = applyMapperCommit.operation().getTaskName();
            String applyAddress = applyMapperCommit.operation().getAddress();
            long ttl = applyMapperCommit.operation().getTtl();

            TaskEntry newEntry = new TaskEntry();
            newEntry.setTaskName(taskName);
            newEntry.setMapperAddress(applyAddress);

            long now = System.currentTimeMillis();
            if( now > ttl ){
                Result result =  ResultFactory.newFailResult(ResultCodeConstant.MAPPER_APPLY_EXPIRED);
                result.setMessage("ttl is:"+ttl);
                return result;
            }

            TaskEntry old = (TaskEntry) table.put(wrapKey(TaskStates.ElectMapper, taskName), newEntry);
            if (old == null) {
                logger.debug("elect mapper success,mapper info:{},session:{}", newEntry,applyMapperCommit.session().state());
                long duration = applyMapperCommit.operation().getTtl()-System.currentTimeMillis();
                executor.schedule(Duration.ofMillis(duration), () -> {
                    logger.debug("it's time to remove mapper,taskName:{}",taskName);
                    table.remove(wrapKey(TaskStates.ElectMapper, taskName));
                });


                return ResultFactory.newSuccessResult();
            }

            //已经选举过mapper
            logger.debug("have already elect mapper,mapper info:{}", old);
            return ResultFactory.newFailResult(ResultCodeConstant.MAPPER_APPLY_FAILED);
        }finally {
            applyMapperCommit.release();
        }

    }

    /**
     * 包装key 防止key冲突
     *
     * @param taskStates
     * @param key
     * @return
     */
    private String wrapKey(TaskStates taskStates, String key) {
        switch (taskStates) {
            case ElectMapper:
                return "t_" + key;
            case Mapping:
                return "m_" + key;
            case Reducing:
                return "r_" + key;
        }
        return "d_" + key;
    }


    /**
     * 同步mapper结果
     *
     * @param syncMapResultCommandCommit
     * @return
     */
    public Result syncMapResult(Commit<SyncMapResultCommand> syncMapResultCommandCommit) {
        try {

            String taskName = syncMapResultCommandCommit.operation().getTaskName();

            if(!isTaskRunning( taskName )){
                return null;
            }


            SyncMapResultEvent syncMapResultEvent = new SyncMapResultEvent(taskName);
            syncMapResultEvent.setMapResult(syncMapResultCommandCommit.operation().getMapResult());
            sessions.forEach(session -> {
                session.publish(SyncMapResultEvent.getEventName(taskName), syncMapResultEvent);
            });

        } finally {
            syncMapResultCommandCommit.close();
        }

        return ResultFactory.newSuccessResult();
    }

    /**
     * task是否有效
     * @param taskName
     * @return
     */
    private boolean isTaskRunning(String taskName) {
        return table.get(wrapKey(TaskStates.ElectMapper, taskName))!=null;
    }


    /**
     * 同步result结果
     *
     * @param syncReduceResultCommandCommit
     * @return
     */
    public Result syncReduceResult(Commit<SyncReduceResultCommand> syncReduceResultCommandCommit) {
        try {

            String taskName = syncReduceResultCommandCommit.operation().getTaskName();

            if(!isTaskRunning( taskName )){
                return null;
            }

            ReduceResult reduceResult =  syncReduceResultCommandCommit.operation().getReduceResult();
            SyncReduceResultEvent syncReduceResultEvent = new SyncReduceResultEvent(taskName);
            syncReduceResultEvent.setReduceResult(reduceResult);
            sessions.forEach(session -> {
                session.publish(SyncReduceResultEvent.getEventName(taskName), syncReduceResultEvent);
            });

        } finally {
            syncReduceResultCommandCommit.close();
        }

        return ResultFactory.newSuccessResult();
    }


}
