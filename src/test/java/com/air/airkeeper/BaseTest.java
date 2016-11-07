package com.air.airkeeper;

import com.air.airkeeper.ditributeworker.core.Cluster;
import com.air.airkeeper.simpleExample.MapEntryEvent;
import com.air.airkeeper.simpleExample.PutCommand;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by liukunyang on 16-6-13.
 *
 * @author liukunyang
 * @date 16-6-13
 */
public class BaseTest {

    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected void initServer() throws InterruptedException {
        String configLine = "127.0.0.1:8701_127.0.0.1:8702_127.0.0.1:8703";
        for (int i = 1; i < 4; i++) {
            Cluster cluster = new Cluster(configLine,"127.0.0.1",8700+i,"/home/liukunyang/develop/logs/air-keeper/copycat870"+i);
            cluster.startWorker();
            Thread.sleep(2*1000);
        }
        Thread.sleep(5 * 1000);
    }


    protected CopycatClient initClient(Address serverAddress){
        CopycatClient client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withConnectionStrategy(ConnectionStrategies.ONCE)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();

        CompletableFuture future = client.connect(serverAddress);
        future.exceptionally(new Function() {
            @Override
            public Object apply(Object o) {
                System.out.println(o);
                return null;
            }
        });

        future.whenComplete(new BiConsumer() {
            @Override
            public void accept(Object o, Object o2) {
                System.out.println("+++++"+o);
            }
        });
        future.thenRun(() -> System.out.println("Successfully connected to the cluster!")).join();


        client.serializer().register(PutCommand.class);
        client.serializer().register(MapEntryEvent.class);
        client.onEvent("change", () -> System.out.println( " changed to " ));

        return client;
    }

}
