package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ExtendedCoreComponent;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = { ExtendedCoreComponent.class,ComponentExporter.class},
        resourceType = ExtendedCoreComponentImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ExtendedCoreComponentImpl implements ExtendedCoreComponent {

    static final String RESOURCE_TYPE = "wknd/components/customcorecomponent";

    @Self
    @Via(type = ResourceSuperType.class)
    protected Text text;

    @ValueMapValue
    @Default(values="Hello World.")
    private String moreText;

    public String getMoreText() {
        return moreText;
    };

    @Override
    public String getText() {
        return text.getText();
    }

    @Override
    public boolean isRichText() {
        return text.isRichText();
    }

    @Override
    public String getExportedType() {
        return ExtendedCoreComponentImpl.RESOURCE_TYPE;
    }
}