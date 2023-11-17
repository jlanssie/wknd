package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import com.adobe.aem.guides.wknd.core.services.conf.CustomServiceConf;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.models.annotations.Default;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = CustomService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        immediate = true
)
@Designate(ocd = CustomServiceConf.class)
@ServiceVendor("Jeremy Lanssiers")
public class CustomServiceImpl implements CustomService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CustomServiceConf configuration;

    private String anotherCustomString = StringUtils.EMPTY;

    @Activate
    public void activate(final CustomServiceConf configuration) {
        logger.info("Custom Service activated.");
        this.configuration = configuration;
    }

    @Modified
    public void modified(final CustomServiceConf configuration) {
        logger.info("Custom Service modified.");
        this.configuration = configuration;
    }

    @Deactivate
    public void deactivate(final CustomServiceConf configuration) {
        logger.info("Custom Service deactivated.");
        this.configuration = configuration;
    }

    @Override
    public void setAnotherCustomString(String anotherCustomString) {
        if (configuration.enabled()) {
            this.anotherCustomString = anotherCustomString;
            logger.info("Custom Service setString: {}", anotherCustomString);
        } else {
            logger.info("Custom Service not enabled.");
        }
    }

    @Override
    public String getCustomString() {
        logger.info("Custom Service getCustomString: {}", this.anotherCustomString);
        return this.configuration.customString();
    }

    @Override
    public String getAnotherCustomString() {
        logger.info("Custom Service getAnotherCustomString: {}", this.anotherCustomString);
        return this.anotherCustomString;
    }
}