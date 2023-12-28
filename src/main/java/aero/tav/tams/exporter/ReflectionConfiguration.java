package aero.tav.tams.exporter;

import aero.tav.tams.exporter.api.model.BasicPodModel;
import aero.tav.tams.exporter.api.model.PodModel;
import aero.tav.tams.exporter.exception.CustomExceptionResponse;
import aero.tav.tams.exporter.properties.OpenShiftProperties;
import aero.tav.tams.exporter.properties.ZipkinKafkaBrokerProperties;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;


/**
 * @author mehmetkorkut
 * created 1.03.2023 14:08
 * package - aero.tav.tams.monitoring
 * project - tams-monitoring
 */
@Component
@RegisterReflectionForBinding(classes = {
        /*Properties*/
        OpenShiftProperties.class,
        PodModel.class,
        BasicPodModel.class,
        ZipkinKafkaBrokerProperties.class,
        ByteArraySerializer.class,
        CustomExceptionResponse.class
})
public class ReflectionConfiguration {

}

