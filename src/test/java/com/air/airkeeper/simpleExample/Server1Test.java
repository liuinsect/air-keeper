package com.air.airkeeper.simpleExample;

import io.atomix.catalyst.transport.Address;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.cluster.Cluster;
import io.atomix.copycat.server.cluster.Member;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author liukunyang
 * @date 2016/5/28.
 */
public class Server1Test {

    private static Logger logger = LoggerFactory.getLogger(Server1Test.class);

    private Collection<Address> cluster = Arrays.asList(
            new Address("127.0.0.1", 8701),
            new Address("127.0.0.1", 8702),
            new Address("127.0.0.1", 8704)
    );

    public void init(int port) throws InterruptedException {

        CopycatServer.Builder builder = CopycatServer.builder(new Address("127.0.0.1", port))
                .withStateMachine(MyStateMachine::new);


        Storage storage = Storage.builder()
                .withDirectory(new File("/home/liukunyang/develop/logs/copycat1"))
                .withStorageLevel(StorageLevel.DISK)
                .withMinorCompactionInterval(Duration.ofSeconds(5))
                .withMaxEntriesPerSegment(11024)
                .build();

        CopycatServer server = builder.withStorage(storage)
                .build();
        logger.debug(server.name());

        server.join(cluster);
        server.serializer().register(PutCommand.class);
        server.serializer().register(MapEntryEvent.class);

        server.onStateChange(new Consumer<CopycatServer.State>() {
            @Override
            public void accept(CopycatServer.State state) {
                logger.debug("accept:" + state);
            }
        });


        server.cluster().members().forEach(member -> {
            member.onStatusChange(status -> {
                logger.debug(member.id() + " status changed to " + status);
            });
        });

    }

    public static void printInfo(CopycatServer server) {
        Cluster c1 = server.cluster();
        Member leader = c1.leader();
        logger.debug(leader.toString());
    }

    public static void main(String[] args) throws InterruptedException {
        final Server1Test server1Test = new Server1Test();
        try {
            server1Test.init(8701);
        } catch (InterruptedException e) {

        }


    }

}
