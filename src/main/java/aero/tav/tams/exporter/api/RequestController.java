package aero.tav.tams.exporter.api;

import aero.tav.tams.exporter.api.model.BasicPodModel;
import aero.tav.tams.exporter.api.model.PodModel;
import aero.tav.tams.exporter.service.OpenShiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mehmetkorkut
 * created 8.08.2022 10:18
 * package - aero.tav.tams.exporter.controller
 * project - tams-exporter
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class RequestController {

    private final OpenShiftService openShiftService;

    public RequestController(OpenShiftService openShiftService) {
        this.openShiftService = openShiftService;
    }

    @GetMapping("/projects")
    public List<String> getProjectList(@RequestParam(value = "q", required = false) String query) {
        return openShiftService.getProjects(query);
    }

    @GetMapping("/projects/{projectName}/pods")
    public List<PodModel> getPodListForProject(@PathVariable String projectName) {
        return openShiftService.getPodDetailedList(projectName);
    }

    @GetMapping(value = "/projects/{projectName}/pods/table", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getPodDetailedListAsTableForProject(@PathVariable String projectName) {
        return openShiftService.returnPodDetailedListAsTable(projectName);
    }

    @GetMapping("/projects/{projectName}/pods/basic")
    public List<BasicPodModel> getPodBasicListForProject(@PathVariable String projectName) {
        return openShiftService.getPodBasicList(projectName);
    }

    @GetMapping("/pods")
    public List<PodModel> getPodList() {
        return openShiftService.getPodDetailedList(null);
    }

    @GetMapping("/pods/basic")
    public List<BasicPodModel> getPodBasicList() {
        return openShiftService.getPodBasicList(null);
    }

    @GetMapping(value = "/pods/table", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getPodDetailedListAsTable() {
        return openShiftService.returnPodDetailedListAsTable(null);
    }
}
