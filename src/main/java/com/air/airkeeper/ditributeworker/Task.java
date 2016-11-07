package com.air.airkeeper.ditributeworker;

import com.air.airkeeper.ditributeworker.domain.MapResult;
import com.air.airkeeper.ditributeworker.domain.ReduceResult;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public interface Task {

    /**
     *  map reduce
     *  map阶段
     * @return
     */
    abstract MapResult map();

    /**
     *  map reduce
     *  reduce阶段
     * @param mapResult
     * @return
     */
    abstract ReduceResult reduce(MapResult mapResult);

    /**
     *
     * @param reduceResult
     */
    abstract void reportReduceResult(ReduceResult reduceResult);
}
