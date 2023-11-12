package com.adobe.aem.guides.wknd.core.services;

import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientService {
    CloseableHttpClient getHttpClient();
}
