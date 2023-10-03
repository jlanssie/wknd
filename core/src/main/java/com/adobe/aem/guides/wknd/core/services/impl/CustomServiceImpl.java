package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import com.adobe.aem.guides.wknd.core.services.conf.CustomServiceConf;
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
@ServiceVendor("Inetum")
public class CustomServiceImpl implements CustomService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private CustomServiceConf configuration;

    private String anotherCustomString;

    @Activate
    public void activate(final CustomServiceConf configuration) {
        LOGGER.info("Custom Service activated.");
        this.configuration = configuration;
    }

    @Modified
    public void modified(final CustomServiceConf configuration) {
        LOGGER.info("Custom Service modified.");
        this.configuration = configuration;
    }

    @Deactivate
    public void deactivate(final CustomServiceConf configuration) {
        LOGGER.info("Custom Service deactivated.");
        this.configuration = configuration;
    }

    @Override
    public void setAnotherCustomString(String anotherCustomString) {
        if (configuration.enabled()) {
            this.anotherCustomString = anotherCustomString;
            LOGGER.info("Custom Service setString: {}", anotherCustomString);
        } else {
            LOGGER.info("Custom Service not enabled.");
        }
    }

    @Override
    public String getCustomString() {
        LOGGER.info("Simple Service getString: {}", this.anotherCustomString);
        return this.configuration.customString();
    }

    @Override
    public String getAnotherCustomString() {
        LOGGER.info("Simple Service getString: {}", this.anotherCustomString);
        return this.anotherCustomString;
    }
}

