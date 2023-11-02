package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import com.adobe.aem.guides.wknd.core.utils.TestUtil;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CustomModelTest {
    private final AemContext aemContext = new AemContext();
    private static final String MOCK_JCR_PATH = TestUtil.getMockJcrPath("custom");
    private static final String MOCK_DATA_PATH = "/components/custom/custom.json";
    private static final String EMPTY_MOCK_DATA_PATH = "/components/custom/custom-empty.json";

    @Mock
    private CustomService customService;

    @InjectMocks
    private CustomModel customModel;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForPackage(TestUtil.CORE_PACKAGES);
        aemContext.load().json("/pages/pages.json", "/content");

        lenient().when(customService.getCustomString()).thenReturn("customStringFromCustomService");
        lenient().when(customService.getAnotherCustomString()).thenReturn("anotherCustomStringFromCustomService");
    }

    @Test
    void emptyComponent() {
        aemContext.registerService(CustomService.class, customService);

        customModel = TestUtil.loadSlingModel(aemContext, CustomModel.class, EMPTY_MOCK_DATA_PATH, MOCK_JCR_PATH);

        assertEquals("", customModel.getTitle());
        assertEquals("", customModel.getText());
        assertEquals("customStringFromCustomService", customModel.getCustomStringFromService());
        assertEquals("anotherCustomStringFromCustomService", customModel.getAnotherCustomStringFromService());
    }

    @Test
    void emptyComponentCustomServiceUnavailable() {
        customModel = TestUtil.loadSlingModel(aemContext, CustomModel.class, EMPTY_MOCK_DATA_PATH, MOCK_JCR_PATH);

        assertEquals("", customModel.getTitle());
        assertEquals("", customModel.getText());
        assertEquals("Custom Service is not available.", customModel.getCustomStringFromService());
        assertEquals("Another custom string is null.", customModel.getAnotherCustomStringFromService());
    }

    @Test
    void component() {
        aemContext.registerService(CustomService.class, customService);

        customModel = TestUtil.loadSlingModel(aemContext, CustomModel.class, MOCK_DATA_PATH, MOCK_JCR_PATH);

        assertEquals("title", customModel.getTitle());
        assertEquals("text", customModel.getText());
        assertEquals("customStringFromCustomService", customModel.getCustomStringFromService());
        assertEquals("anotherCustomStringFromCustomService", customModel.getAnotherCustomStringFromService());
    }

    @Test
    void componentCustomServiceUnavailable() {
        customModel = TestUtil.loadSlingModel(aemContext, CustomModel.class, MOCK_DATA_PATH, MOCK_JCR_PATH);

        assertEquals("title", customModel.getTitle());
        assertEquals("text", customModel.getText());
        assertEquals("Custom Service is not available.", customModel.getCustomStringFromService());
        assertEquals("Another custom string is null.", customModel.getAnotherCustomStringFromService());
    }
}