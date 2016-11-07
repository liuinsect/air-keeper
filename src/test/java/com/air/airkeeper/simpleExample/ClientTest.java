package com.air.airkeeper.simpleExample;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.catalyst.util.Listener;
import io.atomix.copycat.client.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author liukunyang
 * @date 2016/5/28.
 */
public class ClientTest {

    private static Address serverAddress =  new Address("127.0.0.1", 8704);

    public static void main(String[] args) {
        CopycatClient client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withConnectionStrategy(ConnectionStrategies.ONCE)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();

        client.connect(serverAddress).thenRun(() -> System.out.println("Successfully connected to the cluster!")).join();
        client.serializer().register(PutCommand.class);
        client.serializer().register(MapEntryEvent.class);
        client.onEvent("change", new Runnable() {
            @Override
            public void run() {
                System.out.println( " changed to " );
            }
        });
        Listener<MapEntryEvent>  listener = client.onEvent("abc", new Consumer<MapEntryEvent>() {
            @Override
            public void accept(MapEntryEvent event) {
                System.out.println(event.getOldValue() + " changed to " + event.getValue());
                client.close();
            }
        });

        CompletableFuture<Object> future = client.submit(new PutCommand("foo", "Hello world!"));
//        Object result = future.join();
//        System.out.println(result);
//        System.out.println(client.state().name());

        client.recover();

    }
}
