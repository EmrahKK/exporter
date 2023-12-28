package aero.tav.tams.exporter.api.model;

import io.fabric8.kubernetes.api.model.ResourceRequirements;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mehmetkorkut
 * created 8.08.2022 18:11
 * package - aero.tav.tams.exporter.service.dto
 * project - tams-exporter
 */
@Getter
@Setter
@Builder
public class PodModel {

    private String name;
    private String podName;
    private String nodeName;
    private ResourceRequirements resourceRequirements;
    private String image;
    private String version;

}
