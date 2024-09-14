package org.githubrepositorystats.ControllerAndService;

import org.githubrepositorystats.Model.Contributor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributorService {

    // Helper method to generate HTML for contributors
    public static String generateHtmlForContributors(List<Contributor> contributors) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head><meta charset='UTF-8'/>");
        html.append("<link rel='stylesheet' href='src/main/resources/static/contributor.css'/>");
        html.append("<title>Contributor Data</title></head>");
        html.append("<body><div class='container'><h1>Contributors</h1>");

        // Iterate over each contributor and append their HTML using the helper method
        for (Contributor c : contributors) {
            appendContributorHtml(html, c);
        }

        html.append("</div></body>");
        html.append("</html>");
        return html.toString();
    }

    // Helper method to append contributor HTML details
    private static void appendContributorHtml(StringBuilder html, Contributor contributor) {
        // Null checks for each field
        String avatarUrl = contributor.getAvatarUrl() != null ? contributor.getAvatarUrl() : "default-avatar.png";
        String login = contributor.getLogin() != null ? ImageService.escapeHtml(contributor.getLogin()) : "Unknown";
        String contributions = String.valueOf(contributor.getContributions());

        // TODO: display flex blir ikke aktivert pga elementene ikke finnes endafire
        html.append("<div class='contributor'>")
                .append("<img class='contributor-avatar' src='").append(avatarUrl).append("' alt='Avatar'/>")
                .append("<div><p>").append(login).append("</p>")
                .append("<p>Contributions: ").append(contributions).append("</p></div>")
                .append("</div>");
    }
}
