package com.adobe.aem.guides.wknd.core.utils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;

public class TestUtil {

    public static final String CORE_PACKAGES = "com.adobe.aem.guides.wknd.core";
    private static final String JCR_PAGE_PATH = "/content/project/country/language/page/jcr:content/root/container/container/%s";

    public static String getMockJcrPath(String componentName) {
        return String.format(JCR_PAGE_PATH, componentName);
    }

    public static <T> T loadSlingModel(AemContext aemContext, Class<T> clazz, String mockDataPath, String mockJcrPath) {

        boolean isAdaptableFromResource = false;
        boolean isAdaptableFromSlingHttpServletRequest = false;

        Class<?>[] adaptables = clazz.getAnnotation(Model.class).adaptables();

        for (Class<?> adaptable : adaptables) {
            if (StringUtils.equals(adaptable.getName(), Resource.class.getName())) {
                isAdaptableFromResource = true;
            }

            if (StringUtils.equals(adaptable.getName(), SlingHttpServletRequest.class.getName())) {
                isAdaptableFromSlingHttpServletRequest = true;
            }
        }

        if (isAdaptableFromResource) {
            return loadSlingModelViaResource(aemContext, clazz, mockDataPath, mockJcrPath);
        }

        if (isAdaptableFromSlingHttpServletRequest) {
            return loadSlingModelViaSlingHttpServletRequest(aemContext, clazz, mockDataPath, mockJcrPath);
        }

        return null;
    }

    public static <T> T loadSlingModelViaSlingHttpServletRequest(AemContext aemContext, Class<T> clazz, String mockDataPath, String mockJcrPath) {
        aemContext.load().json(mockDataPath, mockJcrPath);

        Resource resource = aemContext.resourceResolver().getResource(mockJcrPath);
        MockSlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        ModelFactory modelFactory = aemContext.getService(ModelFactory.class);
        return modelFactory.createModel(request, clazz);
    }

    public static <T> T loadSlingModelViaResource(AemContext aemContext, Class<T> clazz, String mockDataPath, String mockJcrPath) {
        aemContext.load().json(mockDataPath, mockJcrPath);

        Resource resource = aemContext.resourceResolver().getResource(mockJcrPath);
        ModelFactory modelFactory = aemContext.getService(ModelFactory.class);
        return modelFactory.createModel(resource, clazz);
    }
}