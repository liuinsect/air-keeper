package com.air.airkeeper.simpleExample;

import io.atomix.catalyst.transport.Address;

import java.util.*;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class Test1 {

    public static void main(String[] args) {
        List cluster = new ArrayList();
        for (int i = 0; i < 100; i++) {
            cluster.add(new Address("127.0.0."+i, i));
        }

        final List<Address> synchronizedCollection = Collections.synchronizedList(cluster);
        new Thread(() -> {
            for(Address a : synchronizedCollection){
                System.out.println(a);
            }

        }).start();

        new Thread(()->{

            for (int i = 0; i < 1000; i++) {
                synchronizedCollection.add( new Address("127.0.0.1", 8701));
                synchronizedCollection.remove( new Address("127.0.0.1", 8701));
            }
        }).start();
    }
}

