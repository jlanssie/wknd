package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.utils.TestUtil;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CustomComponentModelTest {
    private final AemContext aemContext = new AemContext();
    private static final String JCR_PATH = TestUtil.getMockJcrPath("custom");
    private static final String MOCK_DATA = "/components/custom-component/custom.json";
    private static final String MOCK_DATA_EMPTY = "/components/custom-component/custom-empty.json";

    @InjectMocks
    private CustomComponentModel customModel;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForPackage(TestUtil.CORE_PACKAGES);
        aemContext.load().json("/pages/pages.json", "/content");
    }

    @Test
    void emptyComponent() {
        customModel = TestUtil.loadSlingModel(aemContext, CustomComponentModel.class, MOCK_DATA_EMPTY, JCR_PATH);

        assertEquals("", customModel.getTitle());
        assertEquals("", customModel.getText());
        assertEquals("Hello World.", customModel.getCustomString());
    }

    @Test
    void emptyComponentCustomServiceUnavailable() {
        customModel = TestUtil.loadSlingModel(aemContext, CustomComponentModel.class, MOCK_DATA_EMPTY, JCR_PATH);

        assertEquals("", customModel.getTitle());
        assertEquals("", customModel.getText());
        assertEquals("Hello World.", customModel.getCustomString());
    }

    @Test
    void component() {
        customModel = TestUtil.loadSlingModel(aemContext, CustomComponentModel.class, MOCK_DATA, JCR_PATH);

        assertEquals("title", customModel.getTitle());
        assertEquals("text", customModel.getText());
        assertEquals("Hello World.", customModel.getCustomString());
    }

    @Test
    void componentCustomServiceUnavailable() {
        customModel = TestUtil.loadSlingModel(aemContext, CustomComponentModel.class, MOCK_DATA, JCR_PATH);

        assertEquals("title", customModel.getTitle());
        assertEquals("text", customModel.getText());
        assertEquals("Hello World.", customModel.getCustomString());
    }
}