package com.air.airkeeper.ditributeworker.core;

import com.air.airkeeper.ditributeworker.AbstractTask;
import com.air.airkeeper.ditributeworker.domain.MapResult;
import com.air.airkeeper.ditributeworker.domain.ReduceResult;
import com.air.airkeeper.ditributeworker.domain.command.ApplyMapperCommand;
import com.air.airkeeper.ditributeworker.domain.command.SyncMapResultCommand;
import com.air.airkeeper.ditributeworker.domain.command.SyncReduceResultCommand;
import com.air.airkeeper.ditributeworker.domain.common.Result;
import com.air.airkeeper.ditributeworker.domain.event.SyncMapResultEvent;
import com.air.airkeeper.ditributeworker.domain.event.SyncReduceResultEvent;
import com.air.airkeeper.ditributeworker.util.AddressUtil;
import com.air.airkeeper.ditributeworker.util.LocalIpUtils;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.catalyst.util.Listener;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by liukunyang on 16-6-13.
 *
 * @author liukunyang
 * @date 16-6-13
 */
public class TaskClient {

    private Logger logger = LoggerFactory.getLogger(TaskClient.class);

    private CopycatClient client;

    private List<Address> serverAddressList;

    private String taskName;

    private AbstractTask task;


    private Listener<SyncMapResultEvent> syncMapResultListener;

    private Listener<SyncReduceResultEvent> syncReduceResultListener;

    private TaskClient(List<Address> serverAddressList, String taskName, AbstractTask task) {
        this.serverAddressList = serverAddressList;
        this.taskName = taskName;
        this.client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withConnectionStrategy(ConnectionStrategies.ONCE)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();
        //TODO 捕捉异常，尝试链接多个server  都连接不上才抛出异常
        client.connect(this.serverAddressList.get(0)).thenRun(() -> logger.info("Successfully connected to the cluster!")).join();
        SerializerFactory.initSerializer(client.serializer());
        this.task = task;
    }

    /**
     * 申请mapper
     * @param timeUnit
     * @param ttl
     * @return
     */
    public Result applyMapper(TimeUnit timeUnit, long ttl) {
        long now = System.currentTimeMillis();
        long added= timeUnit.toMillis(ttl);
        ApplyMapperCommand applyMapperCommand = new ApplyMapperCommand();
        applyMapperCommand.setAddress(LocalIpUtils.getLocalIp());
        applyMapperCommand.setTaskName(this.taskName);
        applyMapperCommand.setTtl(now+added);
        CompletableFuture<Object> future = client.submit(applyMapperCommand);
        Result result = (Result) future.join();
        return result;
    }

    /**
     * 同步mapResult
     * @param mapResult
     * @return
     */
    public Result syncMapResult(MapResult mapResult){

        SyncMapResultCommand syncMapResultCommand = new SyncMapResultCommand();
        syncMapResultCommand.setTaskName(this.taskName);
        syncMapResultCommand.setMapResult(mapResult);
        CompletableFuture<Object> future = client.submit(syncMapResultCommand);
        Result result = (Result) future.join();
        return result;
    }

    public Result syncReduceResult(ReduceResult reduceResult){
        SyncReduceResultCommand syncReduceResultCommand = new SyncReduceResultCommand();
        syncReduceResultCommand.setTaskName(this.taskName);
        syncReduceResultCommand.setReduceResult(reduceResult);
        CompletableFuture<Object> future = client.submit(syncReduceResultCommand);
        Result result = (Result) future.join();
        return result;
    }


    public static TaskClient build(String configLine, String taskName, AbstractTask task){
        List<Address> addresses = AddressUtil.resolveAddress(configLine);
        if( addresses == null ){
            throw new IllegalArgumentException( "serverAddressList is blank" );
        }

        if( taskName == null ){
            throw new IllegalArgumentException( "taskName is blank" );
        }

        TaskClient client = new TaskClient(addresses,taskName,task );
        return client;
    }


    public void initSyncMapResultEvent() {
        Listener<SyncMapResultEvent> listener = client.onEvent(SyncMapResultEvent.getEventName(taskName), new Consumer<SyncMapResultEvent>() {
            @Override
            public void accept(SyncMapResultEvent event) {
                logger.debug("taskName:{},syncMapResultEvent:{},", event.getTaskName(),event);
                client.context().execute(()->{
                    ReduceResult reduceResult = task.reduce(event.getMapResult());
                    syncReduceResult(reduceResult);
                });

            }
        });
        this.syncMapResultListener=listener;
    }


    public void initSyncReduceResultEvent() {
        Listener<SyncReduceResultEvent> listener = client.onEvent(SyncReduceResultEvent.getEventName(taskName), new Consumer<SyncReduceResultEvent>() {
            @Override
            public void accept(SyncReduceResultEvent event) {
                logger.debug("taskName:{},syncMapResultEvent:{},", event.getTaskName(),event);
                client.context().execute(()->{
                    task.reduceEnd(event.getReduceResult());
                });


            }
        });

        this.syncReduceResultListener = listener;
    }

    public void close(){
        if( syncMapResultListener != null ){
            syncMapResultListener.close();
        }

        if( syncReduceResultListener != null ){
            syncReduceResultListener.close();
        }

        client.close();
    }
}
