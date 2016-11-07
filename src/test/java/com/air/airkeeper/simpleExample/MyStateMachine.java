package com.air.airkeeper.simpleExample;

import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.session.ServerSession;
import io.atomix.copycat.server.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liukunyang
 * @date 2016/5/28.
 */
public class MyStateMachine extends StateMachine implements SessionListener {

    private Logger logger = LoggerFactory.getLogger(MyStateMachine.class);

    private Map<Object, Object> map = new HashMap<>();
    private Set<ServerSession> sessions = new HashSet<>();

    public Object put(Commit<PutCommand> commit) {
        Object result = null;
       try{
           result = map.put(commit.operation().key(), commit.operation().value());
           System.out.println("new:"+commit.operation().value()+"old:"+map.get(commit.operation().key()));
           Object o = result;
           sessions.forEach(serverSession -> {
               try {
                   serverSession.publish("abc", new MapEntryEvent(commit.operation().key(), o, commit.operation().value()));
               } catch (Exception e) {
                   System.out.println(e);
               }
           });

       }finally {
//           commit.release();
       }
        return result;
    }


    @Override
    public void register(ServerSession session) {
        sessions.add(session);
    }

    @Override
    public void unregister(ServerSession session) {
    }

    @Override
    public void expire(ServerSession session) {
    }

    @Override
    public void close(ServerSession session) {
        logger.debug("ready to close session");
        sessions.remove(session);
    }

}
