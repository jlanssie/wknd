package com.adobe.aem.guides.wknd.core.jobs;

import com.adobe.aem.guides.wknd.core.services.MyTranslationService;
import com.adobe.aem.guides.wknd.core.utils.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = {JobConsumer.class, TranslationJob.class},
        immediate = true,
        property = {JobConsumer.PROPERTY_TOPICS + "=" + TranslationJob.topic}
)
public class TranslationJob implements JobConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String topic = "translationJob";

    @Reference
    private MyTranslationService myTranslationService;

    public enum DataTypes {
        PATH("path"), PROCESS_ARGS("processArgs");

        private final String dataType;

        DataTypes(String dataType) {
            this.dataType = dataType;
        }

        public String getDataType() {
            return dataType;
        }
    }

    @Override
    public JobResult process(Job job) {
        logger.error(ServiceUtils.PROCESS_STARTED);

        String path = job.getProperty(DataTypes.PATH.getDataType(), StringUtils.EMPTY);
        String processArgs = job.getProperty(DataTypes.PROCESS_ARGS.getDataType(), StringUtils.EMPTY);

        final boolean SUCCESS = myTranslationService.translate(path, processArgs);

        if (!SUCCESS) {
            logger.error("MyTranslationService returns a failure.");
            logger.error(ServiceUtils.PROCESS_STOPPED);
            return JobResult.FAILED;
        }

        logger.error(ServiceUtils.PROCESS_FINISHED);
        return JobResult.OK;
    }

}