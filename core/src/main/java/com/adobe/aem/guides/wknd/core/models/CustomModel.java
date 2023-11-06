package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomModel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private String text;

    private String customStringFromService;
    private String anotherCustomStringFromService;

    @OSGiService
    private CustomService customService;

    @PostConstruct
    protected void init() {
        if (customService != null) {
            customStringFromService = customService.getCustomString();
            anotherCustomStringFromService = customService.getAnotherCustomString();
            logger.info("Custom Service available.");
        } else {
            customStringFromService = "Custom Service is not available.";
            logger.info("Custom Service is not available.");
        }
    }

    public String getCustomStringFromService() {
        return Optional.ofNullable(customStringFromService).orElse("Custom string is null.");
    }

    public String getAnotherCustomStringFromService() {
        return Optional.ofNullable(anotherCustomStringFromService).orElse("Another custom string is null.");
    }
}