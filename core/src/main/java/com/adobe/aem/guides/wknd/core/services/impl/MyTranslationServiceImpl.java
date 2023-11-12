package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.services.HttpClientService;
import com.adobe.aem.guides.wknd.core.services.MyTranslationService;
import com.adobe.aem.guides.wknd.core.services.conf.MyTranslationServiceConf;
import com.adobe.aem.guides.wknd.core.utils.HttpRequestUtils;
import com.adobe.aem.guides.wknd.core.utils.ServiceUtils;
import com.adobe.aem.guides.wknd.core.utils.SlingUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component(
        service = MyTranslationService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        immediate = true)
@Designate(ocd = MyTranslationServiceConf.class)
public class MyTranslationServiceImpl implements MyTranslationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MyTranslationServiceConf configuration;
    private List<String> properties;

    @Reference
    private HttpClientService httpClientService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Activate
    public void activate(final MyTranslationServiceConf configuration) {
        logger.info(ServiceUtils.SERVICE_ACTIVATED);
        this.configuration = configuration;
        this.properties = Arrays.asList(configuration.node_properties());
    }

    @Modified
    public void modified(final MyTranslationServiceConf configuration) {
        logger.info(ServiceUtils.SERVICE_MODIFIED);
        this.configuration = configuration;
        this.properties = Arrays.asList(configuration.node_properties());
    }

    @Deactivate
    public void deactivate(final MyTranslationServiceConf configuration) {
        logger.info(ServiceUtils.SERVICE_DEACTIVATED);
        this.configuration = configuration;
    }

    @Override
    public boolean translate(String path, String additionalArgs) {
        if (configuration.enabled()) {
            logger.debug(ServiceUtils.PROCESS_STARTED + " at path {}", path);

            JsonObject translationObject = getTranslatableObject(path);

            if (translationObject != null) {
                logger.debug("Translatable Object: {}", translationObject);

                String targetLanguage = SlingUtils.getLanguage(resourceResolverFactory, ServiceUtils.SERVICE_USER, path).toString();

                JsonObject translatedObject;

                try {
                    JsonObject responsePayload = requestTranslation(translationObject, targetLanguage);
                    JsonArray choices = responsePayload.getAsJsonArray("choices");
                    JsonObject choice = choices.get(0).getAsJsonObject();
                    JsonObject message = choice.getAsJsonObject("message");
                    String content = message.get("content").getAsString();

                    translatedObject = JsonParser.parseString(content).getAsJsonObject();
                } catch (UnsupportedEncodingException e) {
                    logger.error("Unsupported Encoding Exception", e);
                    translatedObject = null;
                }

                if (translatedObject != null) {
                    logger.debug("Translated Object: {}.", translatedObject);
                    applyTranslatedObject(translatedObject, path);

                    logger.debug(ServiceUtils.PROCESS_FINISHED);
                    return true;
                } else {
                    logger.debug("Translated Object is null");
                }
            } else {
                logger.debug("Translation Object is null.");
            }
            logger.debug(ServiceUtils.PROCESS_STOPPED);
        } else {
            logger.debug(ServiceUtils.SERVICE_DISABLED);
        }

        return false;
    }

    @Override
    public boolean translateChildPages() {
        return configuration.translate_child_pages();
    }

    private JsonObject getTranslatableObject(String path) {
        ResourceResolver resourceResolver = SlingUtils.getResourceResolver(resourceResolverFactory, ServiceUtils.SERVICE_USER);
        if (resourceResolver == null) {
            return null;
        }

        Resource resource = resourceResolver.getResource(path + "/" + JcrConstants.JCR_CONTENT);
        if (resource == null) {
            return null;
        }

        JsonObject translationObject = new JsonObject();

        translateResources(resource, translationObject);

        return translationObject;
    }

    private void translateResources(Resource resource, JsonObject translationObject) {
        translateResource(resource, translationObject);
        resource.listChildren().forEachRemaining(r -> translateResources(r, translationObject));
    }

    private void translateResource(Resource resource, JsonObject translationObject) {
        logger.debug("Searching properties to translate at path {}", resource.getPath());

        JsonObject propertyObj = new JsonObject();

        ValueMap valueMap = resource.getValueMap();
        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
            String key = entry.getKey();

            if (properties.contains(key)) {
                String value = entry.getValue().toString();
                propertyObj.addProperty(key, value);

                logger.debug("Key: {} | Value: {}", key, value);
            }
        }

        if (!propertyObj.entrySet().isEmpty()) {
            translationObject.add(resource.getPath(), propertyObj);
        }
    }

    private JsonObject requestTranslation(JsonObject translationObject, String targetLanguage) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(configuration.llm_endpoint());
        httpPost.addHeader("Content-Type", configuration.llm_content_type());

        // Consider changing plain text token with AEM Crypto?
        httpPost.addHeader("Authorization", configuration.llm_authorization());

        JsonObject payload = new JsonObject();
        payload.addProperty("model", configuration.llm_model());
        payload.add("messages", getMessages(translationObject, targetLanguage));

        httpPost.setEntity(new StringEntity(payload.toString()));

        logger.debug("Payload: {}", payload);
        logger.debug("Http Post: {}", httpPost);

        CloseableHttpClient closeableHttpClient = httpClientService.getHttpClient();

        return HttpRequestUtils.makePostRequestJSONtoJSON(closeableHttpClient, httpPost);
    }

    private JsonArray getMessages(JsonObject translationObject, String targetLanguage) {
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", String.format("Translate the values in the following JSON to %s and reply with a valid JSON object. %s", targetLanguage, translationObject.toString()));

        JsonArray messages = new JsonArray();
        messages.add(message);

        return messages;
    }

    private void applyTranslatedObject(JsonObject jsonObject, String jcrNodePath) {
        for (java.util.Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            logger.debug("JCR node path is: {}", jcrNodePath);

            if (value.isJsonObject()) {
                logger.debug("Key is a node path: {}", key);
                logger.debug("Value is a JsonObject: {}", value);

                applyTranslatedObject(value.getAsJsonObject(), key);
            }

            if (value.isJsonPrimitive()) {
                logger.debug("Key is a property: {}", key);
                logger.debug("Value is a JsonPrimitive: {}", value);

                applyTranslation(jcrNodePath, key, value.getAsString());
            }
        }
    }

    private void applyTranslation(String jcrNodePath, String key, String value) {
        ResourceResolver resourceResolver = SlingUtils.getResourceResolver(resourceResolverFactory, ServiceUtils.SERVICE_USER);
        if (resourceResolver == null) {
            return;
        }

        Resource resource = resourceResolver.getResource(jcrNodePath);
        if (resource == null) {
            return;
        }

        try {
            ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
            if (modifiableValueMap != null) {
                logger.debug("Path: {}. Property: {}. Value: {}.", jcrNodePath, key, value);

                modifiableValueMap.put(key, value);
                resource.getResourceResolver().commit();
            } else {
                logger.error("Modifiable ValueMap is null.");
            }
        } catch (PersistenceException e) {
            logger.error("Could not commit translation changes.");
        }
    }
}
