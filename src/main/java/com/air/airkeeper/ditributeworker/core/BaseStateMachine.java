package com.air.airkeeper.ditributeworker.core;

import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.session.ServerSession;
import io.atomix.copycat.server.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class BaseStateMachine extends StateMachine implements SessionListener {

    private Logger logger = LoggerFactory.getLogger(BaseStateMachine.class);

    protected Map<String, Object> table = new ConcurrentHashMap<>();
    
    protected Set<ServerSession> sessions = Collections.synchronizedSet(new HashSet());


    @Override
    public void register(ServerSession session) {
        logger.debug("ready to register session:{},state:{}",session,session.state());
        printSessionInfo();
        sessions.add(session);
    }

    @Override
    public void unregister(ServerSession session) {
        logger.debug("ready to unregister session:{},state:{}",session,session.state());
        printSessionInfo();
        sessions.remove(session);
    }

    @Override
    public void expire(ServerSession session) {
        logger.debug("ready to expire session:{},state:{}",session,session.state());
        printSessionInfo();
        sessions.remove(session);
    }

    @Override
    public void close(ServerSession session) {
        logger.debug("ready to close session:{},state:{}",session,session.state());
        printSessionInfo();
        sessions.remove(session);
    }

    private void printSessionInfo(){
        if(!logger.isDebugEnabled()){
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("sessions length:"+ sessions.size());
        for(ServerSession serverSession : sessions){
            sb.append("sessions:"+serverSession);
        }
        logger.debug(sb.toString());
    }

}
