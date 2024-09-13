package org.githubrepositorystats.ControllerAndService;

import org.githubrepositorystats.Model.Commit;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
public class HeatmapService {

    //TODO: put datoer i et array for hvert år

    private static int[][] activityCount;

    // Helper method to generate HTML for the heatmap
    public static String generateHtmlForHeatmap(List<Commit> commits, int year) {
        activityCount = new int[12][31];

        /*
        // If leap year
        if(year % 4 == 0) {
            activityCount = new int[366];
        }
        else {
            activityCount = new int[365];
        }
         */

        fillActivityCount(commits);

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

    // If year is not specified, use current year
    public static String generateHtmlForHeatmap(List<Commit> commits) {
        int currentYear = Year.now().getValue();
        return generateHtmlForHeatmap(commits, currentYear);
    }

    // Helper method to append heatmap HTML details
    private static void appendHeatmapHtml(StringBuilder html, List<Commit> commits) {
        // TODO: denne må bruke activity count, if a.length == 366... else...
    }

    // Styling for Heatmap
    public static String cssHeatmap() {
        String css = "<style>";

        css += "</style>";

        return css;
    }

    private static void fillActivityCount(List<Commit> commits) {
        // TODO: loop gjennom commits, der dato matcher fyll verdi++;

        for(Commit c : commits) {
            LocalDate d = c.getDate();
            int day = d.getDayOfMonth();
            int month = d.getMonthValue();
            System.out.println(d);
            System.out.println("Day: " + day + " - Month: " + month);
            System.out.println("---");

            activityCount[month-1][day-1]++;
        }

        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 31; j++) {
                System.out.println(activityCount[i][j]);
            }
        }
    }
}
