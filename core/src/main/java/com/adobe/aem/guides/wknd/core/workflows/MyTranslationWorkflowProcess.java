package com.adobe.aem.guides.wknd.core.workflows;

import com.adobe.aem.guides.wknd.core.jobs.TranslationJob;
import com.adobe.aem.guides.wknd.core.services.MyTranslationService;
import com.adobe.aem.guides.wknd.core.utils.ServiceUtils;
import com.adobe.aem.guides.wknd.core.utils.SlingUtils;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = {WorkflowProcess.class},
        property = {
                "process.label=My Translation Workflow Process",
                "process.description=Translates pages."
        }
)
public class MyTranslationWorkflowProcess implements WorkflowProcess {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String processArgs;
    private boolean translateChildPages;

    @Reference
    private JobManager jobManager;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private MyTranslationService myTranslationService;


    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        logger.debug(ServiceUtils.PROCESS_STARTED);

        final String PAYLOAD = workItem.getWorkflowData().getPayload().toString();
        final String USERID = workItem.getWorkflowData().getMetaDataMap().get("userId").toString();

        if (metaDataMap.containsKey("PROCESS_ARGS")){
            processArgs = metaDataMap.get("PROCESS_ARGS",String.class);
        } else {
            processArgs = StringUtils.EMPTY;
        }

        logger.debug("Payload: {} | UserId: {} | Process Args: {}", PAYLOAD, USERID, processArgs);

        PageManager pageManager = SlingUtils.getPageManager(resourceResolverFactory, ServiceUtils.SERVICE_USER);
        if (pageManager == null) {
            logger.debug(ServiceUtils.PROCESS_STOPPED);
            return;
        }

        Page page = pageManager.getPage(PAYLOAD);

        if (myTranslationService != null) {
            translateChildPages = myTranslationService.translateChildPages();
        } else {
            translateChildPages = false;
        }

        if (translateChildPages) {
            logger.debug("Starting translation jobs for {} and its child pages.", page.getPath());
            translateTree(page);
        } else {
            logger.debug("Starting translation job for {}.", page.getPath());
            translatePage(page);
        }

        logger.debug(ServiceUtils.PROCESS_FINISHED);
    }

    private void translateTree(Page page) {
        translatePage(page);

        page.listChildren().forEachRemaining(this::translateTree);
    }

    private void translatePage(Page page) {
        Map<String, Object> map = new HashMap<>();
        map.put(TranslationJob.DataTypes.PATH.getDataType(), page.getPath());
        map.put(TranslationJob.DataTypes.PROCESS_ARGS.getDataType(), processArgs);

        jobManager.addJob(TranslationJob.topic, map);
    }
}