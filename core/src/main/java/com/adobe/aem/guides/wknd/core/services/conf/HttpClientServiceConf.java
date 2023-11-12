package com.adobe.aem.guides.wknd.core.services.conf;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "My Http Client Service",
        description = "Create HTTP Clients."
)
public @interface HttpClientServiceConf {
    @AttributeDefinition(
            name = "Time To Live",
            description = "Time to live in ms.", type = AttributeType.INTEGER)
    int time_to_live() default 50000;

    @AttributeDefinition(
            name = "Connection timeout",
            description = "Time before timeout in ms.", type = AttributeType.INTEGER)
    int connection_timeout() default 50000;

    @AttributeDefinition(
            name = "Max per route",
            description = "Maximum connections per route.", type = AttributeType.INTEGER)
    int max_per_route() default 20;

    @AttributeDefinition(
            name = "Max total",
            description = "Maximum connections in total.", type = AttributeType.INTEGER)
    int max_total() default 20;

    @AttributeDefinition(
            name = "Keep-Alive",
            description = "Keep connections alive with Keep-Alive header or time to live.",
            type = AttributeType.BOOLEAN)
    boolean keep_alive() default true;
}