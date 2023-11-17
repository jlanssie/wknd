package com.adobe.aem.guides.wknd.core.models.impl;

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
class ExtendedCoreComponentImplTest {
    private final AemContext aemContext = new AemContext();

    private static final String JCR_PATH = TestUtil.getMockJcrPath("extendedCoreComponent");
    private static final String MOCK_DATA = "/components/extendedCoreComponent/extendedCoreComponent.json";
    private static final String MOCK_DATA_EMPTY = "/components/extendedCoreComponent/extendedCoreComponent-empty.json";

    @Mock
    private Text text;

    @InjectMocks
    private ExtendedCoreComponentImpl extendedCoreComponent;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForPackage(TestUtil.CORE_PACKAGES);
        aemContext.load().json("/pages/pages.json", "/content");
    }

    @Test
    void emptyComponent() {
        extendedCoreComponent = TestUtil.loadSlingModelViaSlingHttpServletRequest(aemContext, ExtendedCoreComponentImpl.class, MOCK_DATA_EMPTY, JCR_PATH);

        assertEquals("Hello World.", extendedCoreComponent.getMoreText());
    }

    @Test
    void component() {
        extendedCoreComponent = TestUtil.loadSlingModelViaSlingHttpServletRequest(aemContext, ExtendedCoreComponentImpl.class, MOCK_DATA, JCR_PATH);

        assertEquals("moreText", extendedCoreComponent.getMoreText());
    }

    @Test
    void coreComponent() {
        extendedCoreComponent = new ExtendedCoreComponentImpl();
        extendedCoreComponent.text = text;

        when(text.isRichText()).thenReturn(true);
        when(text.getText()).thenReturn("abc");

        assertTrue(extendedCoreComponent.isRichText());
        assertEquals("abc", extendedCoreComponent.getText());
    }
}