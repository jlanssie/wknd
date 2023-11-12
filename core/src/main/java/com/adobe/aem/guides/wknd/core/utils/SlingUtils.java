package com.adobe.aem.guides.wknd.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.adobe.aem.guides.wknd.core.utils.ServiceUtils.getAuthInfo;

public class SlingUtils {

    private static final Logger logger = LoggerFactory.getLogger(SlingUtils.class);
    
    public static ResourceResolver getResourceResolver(ResourceResolverFactory resourceResolverFactory, String serviceUser) {
        final Map<String, Object> authInfo = getAuthInfo(serviceUser);

        ResourceResolver resourceResolver;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo);
            logger.debug("{} running ResourceResolver", serviceUser);
        } catch ( LoginException e) {
            logger.error("Login failed. ResourceResolver could not be retrieved.", e);
            return null;
        }

        return resourceResolver;
    }

    public static PageManager getPageManager(ResourceResolverFactory resourceResolverFactory, String serviceUser) {
        ResourceResolver resourceResolver = getResourceResolver(resourceResolverFactory, serviceUser);;

        PageManager pageManager = Optional.ofNullable(resourceResolver).map(rr -> rr.adaptTo(PageManager.class)).orElse(null);
        if (pageManager == null) {
            logger.error("PageManager could not be retrieved.");
            return null;
        }

        return pageManager;
    }

    public static Locale getLanguage(ResourceResolverFactory resourceResolverFactory, String serviceUser, String path) {
        PageManager pageManager = getPageManager(resourceResolverFactory, serviceUser);;
        if (pageManager != null){

            Page page = pageManager.getPage(path);
            if (page != null) {
                return page.getLanguage();
            }
        }

        return new Locale ("en", "US");
    }
}
