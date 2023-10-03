package com.adobe.aem.guides.wknd.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface CustomCoreComponentModel extends Text {
    String getMoreText();
}
