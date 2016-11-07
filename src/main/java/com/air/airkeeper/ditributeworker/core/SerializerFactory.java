package com.air.airkeeper.ditributeworker.core;

import com.air.airkeeper.ditributeworker.domain.MapResult;
import com.air.airkeeper.ditributeworker.domain.ReduceResult;
import com.air.airkeeper.ditributeworker.domain.SyncResult;
import com.air.airkeeper.ditributeworker.domain.command.ApplyMapperCommand;
import com.air.airkeeper.ditributeworker.domain.command.SyncMapResultCommand;
import com.air.airkeeper.ditributeworker.domain.command.SyncReduceResultCommand;
import com.air.airkeeper.ditributeworker.domain.event.SyncReduceResultEvent;
import com.air.airkeeper.ditributeworker.domain.event.SyncMapResultEvent;
import io.atomix.catalyst.serializer.Serializer;

/**
 * Created by liukunyang on 16-6-14.
 *
 * @author liukunyang
 * @date 16-6-14
 */
public class SerializerFactory {

    public static void initSerializer(Serializer serializer){

        serializer.register(MapResult.class);
        serializer.register(ReduceResult.class);
        serializer.register(SyncResult.class);


        serializer.register(ApplyMapperCommand.class);
        serializer.register(SyncMapResultCommand.class);
        serializer.register(SyncReduceResultCommand.class);


        serializer.register(SyncMapResultEvent.class);
        serializer.register(SyncReduceResultEvent.class);


    }

}
