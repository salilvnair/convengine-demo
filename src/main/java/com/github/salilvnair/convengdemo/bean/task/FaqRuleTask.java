package com.github.salilvnair.convengdemo.bean.task;

import com.github.salilvnair.convengine.engine.rule.task.CeRuleTask;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.entity.CeRule;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component("faqRuleTask")
public class FaqRuleTask implements CeRuleTask {

    public void injectContainerData(EngineSession session, CeRule ceRule) {
        // This is just a sample method to demonstrate how to create a rule task and execute it using the SET_TASK action in rules.
        // In a real implementation, you would have your business logic here to fetch data from a database or an external API and then inject that data into the session context for use in subsequent rules or
        Map<String, Object> contextDict = session.contextDict();
        session.putInputParam("injectedData", "This data was injected by the faqRuleTask's injectContainerData method");
    }

}
