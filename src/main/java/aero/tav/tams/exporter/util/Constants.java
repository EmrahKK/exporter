package aero.tav.tams.exporter.util;

import aero.tav.tams.exporter.api.model.PodModel;
import io.fabric8.kubernetes.api.model.Quantity;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * @author mehmetkorkut
 * created 3.03.2023 09:09
 * package - aero.tav.tams.exporter.util
 * project - tams-exporter
 */
@UtilityClass
public class Constants {

    public static String generateToRow(PodModel podModel) {
        String s = "<tr> \n" +
                   "<td>" + podModel.getName() + "</td> \n" +
                   "<td>" + podModel.getVersion() + "</td> \n" +
                   "<td>" + podModel.getPodName() + "</td> \n" +
                   "<td>" + podModel.getNodeName() + "</td> \n";
        if (podModel.getResourceRequirements() != null) {
            Map<String, Quantity> limits = podModel.getResourceRequirements().getLimits();
            Map<String, Quantity> requests = podModel.getResourceRequirements().getRequests();
            s += getResources(requests);
            s += getResources(limits);
        } else {
            s += """
                    <td> N/A </td>\s
                    <td> N/A </td>\s
                    <td> N/A </td>\s
                    <td> N/A </td>\s
                    """;
        }
        s += "</tr>";
        return s;
    }

    private static String getResources(Map<String, Quantity> quantityMap) {
        if (quantityMap != null && !quantityMap.isEmpty()) {
            Quantity cpu = quantityMap.get("cpu");
            Quantity memory = quantityMap.get("memory");
            return "<td>" + cpu + "</td> \n" +
                   "<td>" + memory + "</td> \n";
        } else {
            return """
                    <td> N/A </td>\s
                    <td> N/A </td>\s
                    """;
        }
    }

    public static String generateHTML(String nameSpace, String rows) {
        return "<html>"
               + "<head>"
               + "<title> " + nameSpace.toUpperCase() + " Pod Version List" + "</title>" +
               "<style>"
               + "table { border-collapse: collapse; width: 100%; font-family: Arial, sans-serif; }"
               + "td, th { border: 1px solid #ddd; text-align: left; padding: 8px; }"
               + "tr:nth-child(even) { background-color: #f2f2f2; }"
               + "th { background-color: #409eff; color: white; }"
               + "td:hover { background-color: #f5f5f5; cursor: pointer; }"
               + ".caption { font-size: 1.2em; font-weight: bold; margin: 10px 0; }"
               + "tfoot { font-weight: bold; }"
               + "@media screen and (max-width: 600px) {"
               + "  table, thead, tbody, th, td, tr { display: block; }"
               + "  thead tr { position: absolute; top: -9999px; left: -9999px; }"
               + "  tr { border: 1px solid #ccc; }"
               + "  td { border: none; border-bottom: 1px solid #eee; position: relative; padding-left: 50%; }"
               + "  td:before { position: absolute; top: 6px; left: 6px; width: 45%; padding-right: 10px; white-space: nowrap; content: attr(data-column); color: #000; font-weight: bold; }"
               + "}"
               + "</style>"
               + "</head>"
               + "<body>"
               + "<table>"
               + "<caption class='caption'>" + nameSpace + " Namespace Pod Version List" + "</caption>"
               + "<thead>"
               + "<tr>"
               + "<th>Name</th>"
               + "<th>Version</th>"
               + "<th>Pod Name</th>"
               + "<th>Node Name</th>"
               + "<th>CPU Request</th>"
               + "<th>Memory Request</th>"
               + "<th>CPU Limit</th>"
               + "<th>Memory Limit</th>"
               + "</tr>"
               + "</thead>"
               + "<tbody>"
               + rows
               + "</tbody>"
               + "<tfoot>"
               + "</tfoot>"
               + "</table>" +
               "</body>"
               + "</html>";
    }
}
