package com.air.airkeeper;

import com.air.airkeeper.ditributeworker.AbstractTask;
import com.air.airkeeper.ditributeworker.domain.MapResult;
import com.air.airkeeper.ditributeworker.domain.ReduceResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by liukunyang on 16-6-14.
 *
 * @author liukunyang
 * @date 16-6-14
 */
public class TaskTest extends BaseTest{

    static Logger logger = LoggerFactory.getLogger(TaskTest.class);

    @Test
    public void start_server() throws InterruptedException, IOException {
        initServer();
        System.in.read();
    }

    @Test
    public void test_1() throws InterruptedException, IOException {
        Task1 task1 = new Task1();
        task1.setConfigLine("127.0.0.1:8701");
        task1.work();
    }


    @Test
    public void parallel_test() throws InterruptedException, IOException {
        Task1 task1 = new Task1();
        task1.setConfigLine("127.0.0.1:8701");

        Task1 task2 = new Task1();
        task2.setConfigLine("127.0.0.1:8701");

        Task1 task3 = new Task1();
        task3.setConfigLine("127.0.0.1:8701");

        Thread.sleep(1000);
        task1.work();
        task2.work();
        task3.work();


        System.in.read();
    }


    public static class Task1 extends AbstractTask {

        @Override
        public String getTaskName() {
            return "task1";
        }

        @Override
        public MapResult map() {
            MapResult mapResult = new MapResult();
            for (int i = 0; i < 3; i++) {
                mapResult.addDefaultModel(String.valueOf(i),i);
            }
            return mapResult;
        }

        @Override
        public ReduceResult reduce(MapResult mapResult) {

            Map map = mapResult.getResult();


            Set keys = map.entrySet();
            Iterator it = keys.iterator();
            while(it.hasNext()){
                Object key = it.next();
                logger.info("reduce key:{},value:{}",key,map.get(key));
            }

            ReduceResult reduceResult = new ReduceResult();
            for (int i = 101; i < 103; i++) {
                reduceResult.addDefaultModel(String.valueOf(i),i);
            }

            try {
                Thread.sleep(1000*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return reduceResult;
        }

        @Override
        public void reduceEnd(ReduceResult reduceResult) {

            Map map = reduceResult.getResult();


            Set keys = map.entrySet();
            Iterator it = keys.iterator();
            while(it.hasNext()){
                Object key = it.next();
                logger.info("reduceEnd key:{},value:{}",key,map.get(key));
            }
        }
    }
}

