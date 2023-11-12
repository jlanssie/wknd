package com.adobe.aem.guides.wknd.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.Collections;
import java.util.Map;

public class ServiceUtils {

    public static final String PROCESS_STARTED = "Process started.";
    public static final String PROCESS_STOPPED = "Process stopped.";
    public static final String PROCESS_FINISHED = "Process finished.";
    public static final String SERVICE_ACTIVATED = "Service activated.";
    public static final String SERVICE_MODIFIED = "Service modified.";
    public static final String SERVICE_DEACTIVATED = "Service deactivated.";
    public static final String SERVICE_DISABLED = "Service disabled.";
    public static final String SERVICE_USER = "wkndservice";

    public static Map<String, Object> getAuthInfo(String serviceUser) {
        if (StringUtils.isBlank(serviceUser)) { serviceUser = StringUtils.EMPTY; }
        return Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, serviceUser);
    }
}

