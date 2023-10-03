package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.services.CustomService;
import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.apache.commons.codec.CharEncoding.UTF_8;

@Component(
        service = {Servlet.class},
        property = {
                org.osgi.framework.Constants.SERVICE_DESCRIPTION + "=" + "Simple Servlet",
                "sling.servlet.methods" + "=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths" + "=" + "/services/custom"
        })
@ServiceDescription("Custom Servlet")
@ServiceVendor("Inetum")
public class CustomServlet extends SlingSafeMethodsServlet {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Reference
    private CustomService customService;

    private static final String PARAM = "string";

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        RequestParameterMap map = request.getRequestParameterMap();

        if (!map.isEmpty()) {
            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.containsKey(PARAM)) {
                handleString(request, response);
            }
        }
    }

    private void handleString(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType(ContentType.DEFAULT_TEXT.getMimeType());
        response.setCharacterEncoding(UTF_8);
        response.setStatus(SlingHttpServletResponse.SC_OK);

        String string = request.getParameter(PARAM);
        customService.setAnotherCustomString(string);

        PrintWriter writer = response.getWriter();
        writer.write("Set string to " + string);
        writer.close();
    }
}
