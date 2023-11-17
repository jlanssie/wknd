package com.adobe.aem.guides.wknd.core.services.conf;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Custom Service",
        description = "Returns a string."
)
public @interface CustomServiceConf {
    @AttributeDefinition(
            name = "Enabled",
            description = "Enable the service to return its Custom String.",
            type = AttributeType.BOOLEAN)
    boolean enabled() default true;

    @AttributeDefinition(
            name = "Custom String",
            description = "Initialize the service with this string.",
            type = AttributeType.STRING
    )
    String customString() default "Hello World!";
}