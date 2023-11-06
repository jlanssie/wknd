package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import com.adobe.aem.guides.wknd.core.services.conf.CustomServiceConf;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class CustomServiceImplTest {

    private final AemContext aemContext = new AemContext();

    @Mock
    private CustomServiceConf customServiceConf;

    @InjectMocks
    private CustomService customService = new CustomServiceImpl();

    @BeforeEach
    void setup() {
        aemContext.load().json("/pages/pages.json", "/content");

        customService = aemContext.registerService(customService);
    }

    @Test
    void enabled() {
        lenient().when(customServiceConf.enabled()).thenReturn(true);
        lenient().when(customServiceConf.customString()).thenReturn("abc");

        assertEquals("abc", customService.getCustomString());
        assertEquals("", customService.getAnotherCustomString());

        customService.setAnotherCustomString("abc");
        assertEquals("abc", customService.getAnotherCustomString());
    }

    @Test
    void disabled() {
        lenient().when(customServiceConf.enabled()).thenReturn(false);
        lenient().when(customServiceConf.customString()).thenReturn("abc");

        assertEquals("", customService.getCustomString());
        assertEquals("", customService.getAnotherCustomString());

        customService.setAnotherCustomString("abc");
        assertEquals("", customService.getAnotherCustomString());
    }

    @Test
    void activate() {
        customService = aemContext.registerInjectActivateService(customService);
        assertNotNull(customService);
    }
}