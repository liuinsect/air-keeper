package com.air.airkeeper.ditributeworker;

import com.air.airkeeper.ditributeworker.core.TaskClient;
import com.air.airkeeper.ditributeworker.domain.MapResult;
import com.air.airkeeper.ditributeworker.domain.ReduceResult;
import com.air.airkeeper.ditributeworker.domain.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by liukunyang on 16-6-13.
 *
 * @author liukunyang
 * @date 16-6-13
 */
public abstract class AbstractTask {

    private Logger logger = LoggerFactory.getLogger(AbstractTask.class);

    private TaskClient client;

    private String configLine;

    public void work() {
        if( client == null ){
            client = TaskClient.build(configLine,getTaskName(),this);
        }
        Result result = client.applyMapper(TimeUnit.MINUTES,1);

        client.initSyncMapResultEvent();

        if( result.isSuccess() ){
            client.initSyncReduceResultEvent();
        }
        logger.info( "result:{}",result );

        if( result.isSuccess() ){
            MapResult mapResult = map();
            Result syncResult = client.syncMapResult(mapResult);
            logger.info( "syncMapResult:{}",syncResult );

        }
    }

    public void setConfigLine(String configLine) {
        this.configLine = configLine;
    }

    public abstract String getTaskName();

    public abstract MapResult map();


    public abstract ReduceResult reduce(MapResult mapResult);


    public abstract void reduceEnd(ReduceResult reduceResult);

    protected void close(){
        this.client.close();
    }
}
