package com.github.salilvnair.convengdemo.bean.task;

import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.engine.task.CeTask;
import org.springframework.stereotype.Component;

@Component("pendingActionTask")
public class PendingActionTask implements CeTask {

    public void withUserConfirmationOfMoveConnectionDoSomething(EngineSession session) {
        session.put("pending_action_result", "EXECUTED");
    }

}
