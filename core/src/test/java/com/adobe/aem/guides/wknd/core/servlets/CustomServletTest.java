package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CustomServletTest {

    @Mock
    RequestParameterMap requestParameterMap;

    @Mock
    Map<String, String[]> parameterMap;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private CustomService customService;

    @InjectMocks
    private CustomServlet customServlet;

    @BeforeEach
    public void setUp() {
        customServlet = new CustomServlet();
        customServlet.customService = customService;
    }

    @Test
    void doGetWithParameter() throws Exception {
        lenient().when(request.getRequestParameterMap()).thenReturn(requestParameterMap);
        lenient().when(requestParameterMap.isEmpty()).thenReturn(false);
        lenient().when(request.getParameterMap()).thenReturn(parameterMap);
        lenient().when(parameterMap.containsKey(anyString())).thenReturn(true);
        lenient().when(request.getParameter(anyString())).thenReturn("string");

        PrintWriter writer = Mockito.mock(PrintWriter.class);
        lenient().when(response.getWriter()).thenReturn(writer);

        customServlet.doGet(request, response);

        Mockito.verify(customService).setAnotherCustomString("string");
        Mockito.verify(writer).write("Set string to string");
    }

    @Test
    void doGetWithoutParameter() throws Exception {
        lenient().when(request.getRequestParameterMap()).thenReturn(requestParameterMap);
        lenient().when(requestParameterMap.isEmpty()).thenReturn(true);

        customServlet.doGet(request, response);

        Mockito.verify(customService, Mockito.never()).setAnotherCustomString(anyString());
    }
}