package com.adobe.aem.guides.wknd.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class HttpRequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    private HttpRequestUtils() {
    }

    public static JsonObject makePostRequestJSONtoJSON(
            CloseableHttpClient closeableHttpClient,
            HttpRequestBase httpRequestBase) {

        Reader reader = null;

        try (CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpRequestBase)) {
            reader = new InputStreamReader(closeableHttpResponse.getEntity().getContent(), StandardCharsets.UTF_8);

            int responseStatus = closeableHttpResponse.getStatusLine().getStatusCode();
            JsonObject responsePayload = new Gson().fromJson(reader, JsonObject.class);

            logger.debug("Response Status: {}.", responseStatus);
            logger.debug("Response Payload: {}.", responsePayload);

            if (responseStatus == 200) {
                return responsePayload;
            }
        } catch (Exception e) {
            logger.error("Exception.", e);
        } finally {
            httpRequestBase.releaseConnection();
            logger.debug("Connection released.");

            closeStream(reader);
        }

        return null;
    }

    private static void closeStream(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
                logger.debug("Stream closed.");
            } catch (IOException e) {
                logger.error("IOException. Cannot close reader.", e);
            }
        }
    }
}