package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import com.adobe.aem.guides.wknd.core.services.conf.CustomServiceConf;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
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
@ServiceVendor("Inetum")
public class CustomServiceImpl implements CustomService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CustomServiceConf configuration;

    private String anotherCustomString;

    @Activate
    public void activate(final CustomServiceConf configuration) {
        logger.info("Custom Service activated.");
        this.configuration = configuration;
    }

    @Deactivate
    public void deactivate() {
        logger.info("Custom Service deactivated.");
    }

    @Override
    public void setAnotherCustomString(String anotherCustomString) {
        if (configuration.enabled()) {
            this.anotherCustomString = anotherCustomString;
            logger.debug("Custom Service setAnotherCustomString: {}", anotherCustomString);
        } else {
            logger.debug("Custom Service not enabled.");
        }
    }

    @Override
    public String getCustomString() {
        if (configuration.enabled()) {
            logger.info("Custom Service getCustomString: {}", this.anotherCustomString);
            return this.configuration.customString();
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public String getAnotherCustomString() {
        if (configuration.enabled()) {
            logger.info("Custom Service getAnotherCustomString: {}", this.anotherCustomString);
            return StringUtils.isNotBlank(this.anotherCustomString) ? this.anotherCustomString : StringUtils.EMPTY;
        } else {
            return StringUtils.EMPTY;
        }
    }
}
