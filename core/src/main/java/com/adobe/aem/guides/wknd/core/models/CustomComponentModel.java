package com.adobe.aem.guides.wknd.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomComponentModel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String text;

    private String customString;

    @PostConstruct
    protected void init() {
        logger.info("Constructing model.");
        customString = "Hello World.";
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getCustomString() {
        return Optional.ofNullable(customString).orElse(StringUtils.EMPTY);
    }
}