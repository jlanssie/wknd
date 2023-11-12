package com.adobe.aem.guides.wknd.core.services;

public interface MyTranslationService {
    boolean translate(String path, String additionalArgs);

    boolean translateChildPages();
}
