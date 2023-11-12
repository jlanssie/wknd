package com.adobe.aem.guides.wknd.core.services.conf;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "My Translation Service",
        description = "Translates pages."
)
public @interface MyTranslationServiceConf {
    @AttributeDefinition(
            name = "Enabled",
            description = "Enable the service's functionalities.",
            type = AttributeType.BOOLEAN)
    boolean enabled() default true;

    @AttributeDefinition(
            name = "Translate Child pages",
            description = "Enables the MyTranslationWorkflowProcess to create translation jobs for child pages of any payload.",
            type = AttributeType.BOOLEAN)
    boolean translate_child_pages() default true;

    @AttributeDefinition(
            name = "Node Properties",
            description = "Defines which node properties should be translated.",
            type = AttributeType.STRING)
    String[] node_properties() default {};

    @AttributeDefinition(
            name = "API Endpoint",
            description = "Defines to which API endpoint translation requests are sent.",
            type = AttributeType.STRING)
    String llm_endpoint() default "";

    @AttributeDefinition(
            name = "Model",
            description = "Defines the LLM's model.",
            type = AttributeType.STRING)
    String llm_model() default "";

    @AttributeDefinition(
            name = "Content Type",
            description = "Defines the POST request's contentType header.",
            type = AttributeType.STRING)
    String llm_content_type() default "application/json";

    @AttributeDefinition(
            name = "Model",
            description = "Defines the POST request's Authorization header.",
            type = AttributeType.STRING)
    String llm_authorization() default "";
}
