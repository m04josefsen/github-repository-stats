package org.githubrepositorystats.ControllerAndService;

import org.githubrepositorystats.Model.Commit;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;

@Service
public class HeatmapService {

    private static int[][] activityCount;

    // Helper method to generate HTML for the heatmap
    public static String generateHtmlForHeatmap(List<Commit> commits, int year) {
        activityCount = new int[12][31];

         fillActivityCount(commits);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head><meta charset='UTF-8'/>");
        html.append("<link rel='stylesheet' href='src/main/resources/static/heatmap.css'/>");
        html.append("</head><body><div class='container'><h1>Commit heatmap</h1>");

        appendHeatmapHtml(html, year);

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    // If year is not specified, use current year
    public static String generateHtmlForHeatmap(List<Commit> commits) {
        int currentYear = Year.now().getValue();
        return generateHtmlForHeatmap(commits, currentYear);
    }

    // Helper method to append heatmap HTML details for normal years
    private static void appendHeatmapHtml(StringBuilder html, int year) {
        HashMap<Integer, Integer> map = fillMonthsWithDays();

        // If leap year, change feburary from 28 -> 29;
        if(year % 4 == 0) {
            map.replace(2, 29);
        }

        html.append("<div class='heatmap' >");

        // Loop through each month to create a new div, then loop through each month to add the days
        for(int i = 0; i < 12; i++) {
            int days = map.get(i+1);

            // Dynamically calculate grid-column based on the loop index
            int startColumn = i + 1;
            int endColumn = startColumn + 1;
            System.out.println(startColumn + " / " + endColumn);

            // Append the dynamic grid-column style to the HTML
            html.append("<div class='month' style='grid-column: ")
                    .append(startColumn)
                    .append(" / ")
                    .append(endColumn)
                    .append(";'>");

            for(int j = 0; j < days; j++) {
                int commitCount = activityCount[i][j];

                // TODO: midlertidig, kun for test, skal endres for mørkere farge hvis flere commits
                if(commitCount > 0) {
                    html.append("<div class='day active'>"); // Used as a box for coloring
                }
                else {
                    html.append("<div class='day inactive'>"); // Used as a box for coloring
                }

                html.append("</div>"); // Closing day
            }

            html.append("</div>"); // Closing month
        }

        html.append("</div>"); // Closing heatmap
    }

    // Method to fill activity count array with dates
    private static void fillActivityCount(List<Commit> commits) {

        for(Commit c : commits) {
            LocalDate d = c.getDate();
            int day = d.getDayOfMonth();
            int month = d.getMonthValue();

            activityCount[month-1][day-1]++;
        }
    }

    // Method to fill how many days each month has
    private static HashMap<Integer, Integer> fillMonthsWithDays() {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 31);
        map.put(2, 28);
        map.put(3, 31);
        map.put(4, 30);
        map.put(5, 31);
        map.put(6, 30);
        map.put(7, 31);
        map.put(8, 31);
        map.put(9, 30);
        map.put(10, 31);
        map.put(11, 30);
        map.put(12, 31);

        return map;
    }



}
