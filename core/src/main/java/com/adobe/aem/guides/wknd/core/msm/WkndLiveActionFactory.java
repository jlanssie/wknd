package com.adobe.aem.guides.wknd.core.msm;

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.ActionConfig;
import com.day.cq.wcm.msm.api.LiveAction;
import com.day.cq.wcm.msm.api.LiveActionFactory;
import com.day.cq.wcm.msm.api.LiveRelationship;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Component(
        service={LiveActionFactory.class},
        property={LiveActionFactory.LIVE_ACTION_NAME + "=" + WkndLiveActionFactory.LIVE_ACTION_NAME}
)
public class WkndLiveActionFactory implements LiveActionFactory<LiveAction> {
    static final String LIVE_ACTION_NAME = "wkndAction";

    public LiveAction createAction(Resource config) {
        ValueMap configs;
        if (config == null || config.adaptTo(ValueMap.class) == null) {
            configs = new ValueMapDecorator(Collections.<String, Object>emptyMap());
        } else {
            configs = config.adaptTo(ValueMap.class);
        }

        return new WkndLiveAction(LIVE_ACTION_NAME, configs);
    }

    public String createsAction() {
        return LIVE_ACTION_NAME;
    }

    /************* LiveAction ****************/
    private static class WkndLiveAction implements LiveAction {
        private String name;
        private ValueMap configs;
        private static final Logger log = LoggerFactory.getLogger(WkndLiveAction.class);

        public WkndLiveAction(String nm, ValueMap config) {
            name = nm;
            configs = config;
        }

        public void execute(Resource source, Resource target, LiveRelationship liverel, boolean autoSave, boolean isResetRollout) throws WCMException {
            log.info(" *** Executing WkndLiveAction *** ");
        }

        public String getName() {
            return name;
        }

        /************* Deprecated *************/
        @Deprecated
        public void execute(ResourceResolver arg0, LiveRelationship arg1,
                            ActionConfig arg2, boolean arg3) throws WCMException {
        }

        @Deprecated
        public void execute(ResourceResolver arg0, LiveRelationship arg1,
                            ActionConfig arg2, boolean arg3, boolean arg4)
                throws WCMException {
        }

        @Deprecated
        public String getParameterName() {
            return null;
        }

        @Deprecated
        public String[] getPropertiesNames() {
            return null;
        }

        @Deprecated
        public int getRank() {
            return 0;
        }

        @Deprecated
        public String getTitle() {
            return null;
        }

        @Deprecated
        public void write(JSONWriter arg0) throws JSONException {
        }
    }
}
