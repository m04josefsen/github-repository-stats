package org.githubrepositorystats.ControllerAndService;

import org.githubrepositorystats.Model.Commit;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class HeatmapService {

    // Helper method to generate HTML for the heatmap
    public static String generateHtmlForHeatmap(List<Commit> commits, int year) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head><meta charset='UTF-8'/>");
        html.append(cssHeatmap());
        html.append("</head><body><div class='container'><h1>Commit heatmap</h1>");

        appendHeatmapHtml(html,commits);

        html.append("</div></body></html>");
        return html.toString();
    }

    // If year not specified, use current year
    public static String generateHtmlForHeatmap(List<Commit> commits) {
        int currentYear = Year.now().getValue();
        return generateHtmlForHeatmap(commits, currentYear);
    }

    // Helper method to append heatmap HTML details
    private static void appendHeatmapHtml(StringBuilder html, List<Commit> commits) {

    }

    // Styling for Heatmap
    public static String cssHeatmap() {
        String css = "<style>";

        css += "</style>";

        return css;
    }
}
