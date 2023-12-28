package aero.tav.tams.exporter.service.impl;

import aero.tav.tams.exporter.api.model.BasicPodModel;
import aero.tav.tams.exporter.api.model.PodModel;
import aero.tav.tams.exporter.properties.OpenShiftProperties;
import aero.tav.tams.exporter.service.OpenShiftService;
import aero.tav.tams.exporter.util.Constants;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.api.model.ProjectList;
import io.fabric8.openshift.client.OpenShiftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

/**
 * @author mehmetkorkut
 * created 8.08.2022 17:39
 * package - aero.tav.tams.exporter.service.impl
 * project - tams-exporter
 */
@Service
@Slf4j
public class OpenShiftServiceImpl implements OpenShiftService {

    private final OpenShiftProperties openShiftProperties;

    public OpenShiftServiceImpl(OpenShiftProperties openShiftProperties) {
        this.openShiftProperties = openShiftProperties;
    }

    @Override
    public List<String> getProjects(String query) {
        ProjectList projectList = getProjectList();
        return projectList
                .getItems()
                .stream()
                .filter(project -> StringUtils.isBlank(query) || project.getMetadata().getName().toLowerCase().contains(query.toLowerCase()))
                .map(project -> project.getMetadata().getName())
                .toList();
    }

    @Override
    public List<PodModel> getPodDetailedList(String projectName) {
        PodList list = getPodList(projectName);
        return list.getItems()
                .stream()
                .map(pod -> {
                    PodSpec spec = pod.getSpec();
                    Container container = spec.getContainers().get(0);
                    String image = container.getImage();
                    String[] split = image.split(":");
                    return PodModel.builder()
                            .name(container.getName())
                            .podName(pod.getMetadata().getName())
                            .image(image)
                            .version(split.length > 1 ? split[1] : "N/A")
                            .nodeName(spec.getNodeName())
                            .resourceRequirements(container.getResources())
                            .build();
                }).toList();
    }

    @Override
    public List<BasicPodModel> getPodBasicList(String projectName) {
        PodList list = getPodList(projectName);
        return list.getItems()
                .stream()
                .map(pod -> {
                    Container container = pod.getSpec().getContainers().get(0);
                    return BasicPodModel.builder()
                            .image(container.getImage())
                            .build();
                }).toList();
    }

    @Override
    public String returnPodDetailedListAsTable(String projectName) {
        List<PodModel> podDetailedList = getPodDetailedList(projectName).stream().sorted(Comparator.comparing(PodModel::getName)).toList();
        StringBuilder rows = new StringBuilder();
        podDetailedList.forEach(podModel -> rows.append(Constants.generateToRow(podModel)));
        return Constants.generateHTML(StringUtils.isBlank(projectName) ? openShiftProperties.getNameSpace() : projectName, rows.toString());
    }

    private PodList getPodList(String projectName) {
        try (OpenShiftClient openShiftClient = createOpenShiftClient()) {
            return openShiftClient.pods().inNamespace(StringUtils.isBlank(projectName) ? openShiftProperties.getNameSpace() : projectName).list();
        }
    }

    private ProjectList getProjectList() {
        try (OpenShiftClient openShiftClient = createOpenShiftClient()) {
            return openShiftClient.projects().list();
        }
    }

    private OpenShiftClient createOpenShiftClient() {
        return new KubernetesClientBuilder()
                .withConfig(getConfig())
                .build()
                .adapt(OpenShiftClient.class);
    }


    private Config getConfig() {
        log.debug("OpenShiftProperties {}", openShiftProperties.toString());
        if (openShiftProperties.isUsingCertFile()) {
            log.debug("Using cert file");
            return new ConfigBuilder()
                    .withMasterUrl(openShiftProperties.getUrl())
                    .withCaCertFile(openShiftProperties.getCertFile())
                    .withOauthToken(getToken(openShiftProperties.getTokenFile()))
                    .build();
        } else {
            log.debug("Using jwt token");
            return new ConfigBuilder()
                    .withMasterUrl(openShiftProperties.getUrl())
                    .withOauthToken(openShiftProperties.getToken())
                    .build();
        }
    }

    private String getToken(String tokenFile) {
        String token = null;
        try {
            token = new String(Files.readAllBytes(Paths.get(tokenFile)));
        } catch (IOException e) {
            log.error("Exception Reading file, cause: {}", ExceptionUtils.getMessage(e));
        }
        return token;
    }


}
