package com.air.airkeeper.ditributeworker.core;

import com.air.airkeeper.ditributeworker.util.AddressUtil;
import com.air.airkeeper.ditributeworker.util.ListUtils;
import com.air.airkeeper.ditributeworker.util.LocalIpUtils;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.util.Assert;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.List;

/**
 * @author liukunyang
 * @date 2016/6/5.
 */
public class Cluster {


    private static Logger logger = LoggerFactory.getLogger(Cluster.class);

    /**
     * 地址列表
     */
    private List<Address> clusterAddressList;

    /**
     * 本地地址
     */
    private Address localAddress;



    /**
     * copycat日志路径
     */
    private String logPath;


    public Cluster(String configLine,String logPath) {
        List<Address> resolveAddress = AddressUtil.resolveAddress(configLine);
        if(ListUtils.isBlank(resolveAddress)){
            throw new RuntimeException( "inetSocketAddressList is blank" );
        }

        String localAddress = LocalIpUtils.getLocalIp();

        this.clusterAddressList = resolveAddress;
        for(Address address : resolveAddress){
            if( address.host().equals(localAddress) ){
                this.localAddress = new Address(new Address(address.host(),address.port()));
            }
        }


        if( localAddress == null ){
            throw new RuntimeException( "localAddress is blank" );
        }

        Assert.notNull(logPath, "logPath");
        this.logPath=logPath;
    }


    public Cluster(String configLine,String localIp,Integer localPort,String logPath) {
        List<Address> resolveAddress = AddressUtil.resolveAddress(configLine);
        if(ListUtils.isBlank(resolveAddress)){
            throw new RuntimeException( "inetSocketAddressList is blank" );
        }

        String localAddress = LocalIpUtils.getLocalIp();
        this.clusterAddressList = resolveAddress;
        this.localAddress = new Address(new Address(localIp,localPort));

        if( localAddress == null ){
            throw new RuntimeException( "localAddress is blank" );
        }
        Assert.notNull(logPath, "logPath");

        this.logPath=logPath;
    }


    public void startWorker() {
        CopycatServer.Builder builder = CopycatServer.builder(this.localAddress)
                .withStateMachine(AirKeeperStateMachine::new);


        Storage storage = Storage.builder()
                .withDirectory(new File(this.logPath))
                .withStorageLevel(StorageLevel.DISK)
                .withMinorCompactionInterval(Duration.ofSeconds(2))
                .withMaxEntriesPerSegment(256)
                .build();

        CopycatServer server = builder.withStorage(storage)
                .build();



        server.join(this.clusterAddressList);
        SerializerFactory.initSerializer(server.serializer());
        server.onStateChange(state ->
            logger.debug("accept:" + state)
        );

        server.cluster().members().forEach(member -> {
            member.onStatusChange(status -> {
                logger.debug(member.id() + " status changed to " + status);
            });
        });
    }
}
