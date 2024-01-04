package com.adobe.aem.guides.wknd.core.msm;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.msm.api.ActionConfig;
import com.day.cq.wcm.msm.api.LiveAction;
import com.day.cq.wcm.msm.api.LiveRelationship;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class WkndLiveAction implements LiveAction {
    private String name;
    private ValueMap configs;
    private static final Logger LOG = LoggerFactory.getLogger(WkndLiveAction.class);

    public WkndLiveAction(String nm, ValueMap config) {
        name = nm;
        configs = config;
    }

    public void execute(Resource source, Resource target, LiveRelationship liverel, boolean autoSave, boolean isResetRollout) {

        String lastMod = null;

        LOG.info(" *** Executing WkndLiveAction *** ");

        /* Determine if the LiveAction is configured to copy the cq:lastModifiedBy property */
        if ((Boolean) configs.get("repLastModBy")) {

            /* get the source's cq:lastModifiedBy property */
            if (source != null && source.adaptTo(Node.class) != null) {
                ValueMap sourcevm = source.adaptTo(ValueMap.class);
                lastMod = sourcevm.get(NameConstants.PN_PAGE_LAST_MOD_BY, String.class);
            }

            /* set the target node's la-lastModifiedBy property */
            Session session = null;
            if (target != null && target.adaptTo(Node.class) != null) {
                ResourceResolver resolver = target.getResourceResolver();
                session = resolver.adaptTo(javax.jcr.Session.class);
                Node targetNode;
                try {
                    targetNode = target.adaptTo(javax.jcr.Node.class);
                    targetNode.setProperty("la-lastModifiedBy", lastMod);
                    LOG.info(" *** Target node lastModifiedBy property updated: {} ***", lastMod);
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
            if (autoSave) {
                try {
                    session.save();
                } catch (Exception e) {
                    try {
                        session.refresh(true);
                    } catch (RepositoryException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    /************* Deprecated *************/
    @Deprecated
    public void execute(ResourceResolver arg0, LiveRelationship arg1, ActionConfig arg2, boolean arg3) {
    }

    @Deprecated
    public void execute(ResourceResolver arg0, LiveRelationship arg1, ActionConfig arg2, boolean arg3, boolean arg4) {
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