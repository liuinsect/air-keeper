package com.air.airkeeper;

import com.air.airkeeper.simpleExample.MapEntryEvent;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.util.Listener;
import io.atomix.copycat.client.CopycatClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by liukunyang on 16-6-13.
 *
 * @author liukunyang
 * @date 16-6-13
 */
public class SessionTest extends BaseTest {

    private static Logger logger = LoggerFactory.getLogger(SessionTest.class);

    @Test
    public void sessionTest_server() throws InterruptedException, IOException {
        initServer();
        System.in.read();
    }

    @Test
    public void sessionTest_client() throws InterruptedException, IOException {
        CopycatClient client = initClient(new Address("127.0.0.1", 8705));

        Listener<MapEntryEvent> listener = client.onEvent("abc", new Consumer<MapEntryEvent>() {
            @Override
            public void accept(MapEntryEvent event) {
                System.out.println(event.getOldValue() + " changed to " + event.getValue());
                client.close();
            }
        });

//
//        ChangeReduceListenerCommand reduceListenerCommand = new ChangeReduceListenerCommand();
//        reduceListenerCommand.setAddress("127.0.0.1");
//
//        CompletableFuture<Object> future = client.submit(new ChangeReduceListenerCommand());

//        Object result = future.join();
//        System.out.println(result);
        System.out.println("recover over");
        System.in.read();
    }


}
