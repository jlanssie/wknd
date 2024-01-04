package com.adobe.aem.guides.wknd.core.msm;

import com.day.cq.wcm.msm.api.LiveAction;
import com.day.cq.wcm.msm.api.LiveActionFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Component(
        service = LiveActionFactory.class,
        property = {LiveActionFactory.LIVE_ACTION_NAME + "=" + WkndLiveActionFactory.LIVE_ACTION_NAME})
public class WkndLiveActionFactory implements LiveActionFactory<LiveAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WkndLiveActionFactory.class);

    public static final String LIVE_ACTION_NAME = "wkndAction";

    public LiveAction createAction(Resource config) {
        ValueMap configs;
        /* Adapt the config resource to a ValueMap */
        if (config == null || config.adaptTo(ValueMap.class) == null) {
            configs = new ValueMapDecorator(Collections.emptyMap());
        } else {
            configs = config.adaptTo(ValueMap.class);
        }

        return new WkndLiveAction(LIVE_ACTION_NAME, configs);
    }

    public String createsAction() {
        return LIVE_ACTION_NAME;
    }
}
