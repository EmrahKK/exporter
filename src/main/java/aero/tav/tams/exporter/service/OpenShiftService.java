package aero.tav.tams.exporter.service;

import aero.tav.tams.exporter.api.model.BasicPodModel;
import aero.tav.tams.exporter.api.model.PodModel;

import java.util.List;

/**
 * @author mehmetkorkut
 * created 8.08.2022 17:37
 * package - aero.tav.tams.exporter.service
 * project - tams-exporter
 */
public interface OpenShiftService {

    List<String> getProjects(String query);

    List<PodModel> getPodDetailedList(String projectName);

    List<BasicPodModel> getPodBasicList(String projectName);

    String returnPodDetailedListAsTable(String projectName);
}
