package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.utils.TestUtil;
import com.adobe.cq.wcm.core.components.models.Text;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ExtendedCoreComponentModelTest {
    private final AemContext aemContext = new AemContext();

    private static final String MOCK_JCR_PATH = TestUtil.getMockJcrPath("extendedCoreComponent");
    private static final String MOCK_DATA_PATH = "/components/extendedCoreComponent/extendedCoreComponent.json";
    private static final String EMPTY_MOCK_DATA_PATH = "/components/extendedCoreComponent/extendedCoreComponent-empty.json";

    @Mock
    private Text text;

    @InjectMocks
    private ExtendedCoreComponentModel extendedCoreComponentModel;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForPackage(TestUtil.CORE_PACKAGES);
        aemContext.load().json("/pages/pages.json", "/content");
    }

    @Test
    void emptyComponent() {
        extendedCoreComponentModel = TestUtil.loadSlingModelViaSlingHttpServletRequest(aemContext, ExtendedCoreComponentModel.class, EMPTY_MOCK_DATA_PATH, MOCK_JCR_PATH);

        assertEquals("Hello World.", extendedCoreComponentModel.getMoreText());
    }

    @Test
    void component() {
        extendedCoreComponentModel = TestUtil.loadSlingModelViaSlingHttpServletRequest(aemContext, ExtendedCoreComponentModel.class, MOCK_DATA_PATH, MOCK_JCR_PATH);

        assertEquals("moreText", extendedCoreComponentModel.getMoreText());
    }

    @Test
    void coreComponent() {
        extendedCoreComponentModel = new ExtendedCoreComponentModel();
        extendedCoreComponentModel.text = text;

        when(text.isRichText()).thenReturn(true);
        when(text.getText()).thenReturn("abc");

        assertTrue(extendedCoreComponentModel.isRichText());
        assertEquals("abc",extendedCoreComponentModel.getText());
    }
}