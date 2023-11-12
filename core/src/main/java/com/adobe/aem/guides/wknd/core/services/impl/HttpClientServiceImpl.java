package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.services.HttpClientService;
import com.adobe.aem.guides.wknd.core.services.conf.HttpClientServiceConf;
import com.adobe.aem.guides.wknd.core.utils.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component(
        immediate = true,
        service = HttpClientService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        configurationPid = "com.dxp.aem.core.services.client.HttpClientServiceImpl"
)
@Designate(ocd = HttpClientServiceConf.class)
public class HttpClientServiceImpl implements HttpClientService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private HttpClientServiceConf configuration;
    private HttpClientConnectionManager httpClientConnectionManager;
    private CloseableHttpClient httpClient;

    @Activate
    protected void activate(final HttpClientServiceConf configuration) {
        logger.info("Activated the TME Http Client");
        this.configuration = configuration;
        createHttpClientConnectionManager();
        createHttpClient();
    }

    @Modified
    public void modified(final HttpClientServiceConf configuration) {
        logger.info(ServiceUtils.SERVICE_MODIFIED);
        this.configuration = configuration;
        createHttpClientConnectionManager();
        createHttpClient();
    }

    @Deactivate
    public void deactivate(final HttpClientServiceConf configuration) {
        logger.info(ServiceUtils.SERVICE_DEACTIVATED);
        this.configuration = configuration;
        shutdownClient();
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    private void createHttpClientConnectionManager() {
        logger.debug("Creating HTTP client connection manager.");
        shutdownClient();

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(configuration.time_to_live(), TimeUnit.MILLISECONDS);
        poolingHttpClientConnectionManager.setMaxTotal(configuration.max_total());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(configuration.max_per_route());
        httpClientConnectionManager = poolingHttpClientConnectionManager;
    }

    private void createHttpClient() {
        logger.debug("Creating HTTP client.");

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(generateRequestConfiguration());

        if (httpClientConnectionManager != null) {
            logger.debug("Setting connection manager");
            builder.setConnectionManager(httpClientConnectionManager);
        }

        if (configuration.keep_alive()) {
            logger.debug("Setting Keep-Alive header strategy.");
            builder.setKeepAliveStrategy(generateKeepAliveStrategy());
        }

        httpClient = builder.build();
    }

    private RequestConfig generateRequestConfiguration() {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(RequestConfig.DEFAULT);

        requestConfigBuilder.setCookieSpec(CookieSpecs.IGNORE_COOKIES);

        requestConfigBuilder
                .setConnectTimeout(configuration.connection_timeout())
                .setConnectionRequestTimeout(configuration.connection_timeout());

        return requestConfigBuilder.build();
    }

    private ConnectionKeepAliveStrategy generateKeepAliveStrategy() {
        return (response, context) -> {
            HeaderElementIterator headerElementIterator = new BasicHeaderElementIterator(
                    response.headerIterator("Keep-Alive"));
            while (headerElementIterator.hasNext()) {
                HeaderElement headerElement = headerElementIterator.nextElement();
                String param = headerElement.getName();
                String value = headerElement.getValue();
                if ("timeout".equalsIgnoreCase(param) && StringUtils.isNotEmpty(value)) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid Keep-Alive header timeout on request {}. Defaulting to {} ms",headerElement, configuration.time_to_live());
                        return configuration.time_to_live();
                    }
                }
            }
            return configuration.time_to_live();
        };
    }

    private void shutdownClient() {
        if (httpClientConnectionManager != null) {
            httpClientConnectionManager.shutdown();
        }

        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("Could not close HTTP Client.", e);
            }
        }
    }
}
