package com.github.salilvnair.convengdemo.interceptor;

import com.github.salilvnair.ccf.core.model.ContainerComponentRequest;
import com.github.salilvnair.convengine.container.annotation.ContainerDataInterceptor;
import com.github.salilvnair.convengine.container.interceptor.ContainerDataRequestInterceptor;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import org.springframework.stereotype.Component;

@Component
@ContainerDataInterceptor
public class ZpConversationalEngineContainerInterceptor implements ContainerDataRequestInterceptor {
    @Override
    public void intercept(ContainerComponentRequest containerComponentRequest, EngineSession engineSession) {
        System.out.println("ZpConversationalEngineContainerInterceptor intercepted the request: " + containerComponentRequest);
    }
}
