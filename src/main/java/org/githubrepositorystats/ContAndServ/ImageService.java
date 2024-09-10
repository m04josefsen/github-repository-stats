package org.githubrepositorystats.ContAndServ;

import org.githubrepositorystats.Model.Commit;
import org.githubrepositorystats.Model.Contributor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.Java2DRenderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    // TODO: faktisk bruk disse, fikse border radius på bilde, legg en linje mellom de?,
    //  må få flex i contributors så de er ved siden av hverandre
    private final static String BACKGROUND_COLOR = "rgb(40, 44, 52))";
    private final static String TEXT_COLOR = "rgb(255, 255, 255)";

    // Helper method to generate HTML for contributors
    public static String generateHtmlForContributors(List<Contributor> contributors) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head><meta charset='UTF-8'/>");
        html.append("<link rel='stylesheet' type='text/css' href='src/main/resources/static/contributor.css'/>");
        html.append("<title>Contributor Data</title></head>");
        html.append("<body><div class='container'><h1>Contributors</h1>");

        for (Contributor c : contributors) {
            // Null checks for each field
            String avatarUrl = c.getAvatarUrl() != null ? c.getAvatarUrl() : "default-avatar.png";
            String login = c.getLogin() != null ? escapeHtml(c.getLogin()) : "Unknown";
            String contributions = String.valueOf(c.getContributions()) != null ? String.valueOf(c.getContributions()) : "0";

            html.append("<div class='contributor'>")
                    .append("<img src='").append(avatarUrl).append("' alt='Avatar' />")
                    .append("<div><p>").append(login).append("</p>")
                    .append("<p>Contributions: ").append(contributions).append("</p></div>")
                    .append("</div>");
        }

        html.append("</div></body>");
        html.append("</html>");
        return html.toString();
    }

    // Helper method to generate HTML for commits
    public static String generateHtmlForCommits(List<Commit> commits) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head><meta charset='UTF-8'/>");
        html.append("<link rel='stylesheet' type='text/css' href='src/main/resources/static/commits.css'/>");
        html.append("</head><body><div class='container'><h1>Commits</h1>");

        if (commits != null && !commits.isEmpty()) {
            // Handle the first commit
            Commit firstCommit = commits.get(commits.size() - 1);
            appendCommitHtml(html, firstCommit, "First Commit");

            // Handle the last commit
            if (commits.size() > 1) {
                Commit lastCommit = commits.get(0);
                appendCommitHtml(html, lastCommit, "Latest Commit");
            }
        } else {
            html.append("<p>No commits available</p>");
        }

        html.append("</div></body></html>");
        return html.toString();
    }

    // Helper method to append commit information to the HTML
    private static void appendCommitHtml(StringBuilder html, Commit commit, String label) {
        String avatarUrl = commit.getAvatarUrl() != null ? commit.getAvatarUrl() : "default-avatar.png";
        String login = commit.getLogin() != null ? escapeHtml(commit.getLogin()) : "Unknown";
        String message = commit.getMessage() != null ? escapeHtml(commit.getMessage()) : "No message provided";
        String date = commit.getDate() != null ? commit.getDate().toString() : "Unknown date";

        html.append("<div class='commit'>")
                .append("<h2>").append(label).append("</h2>")
                .append("<img src='").append(avatarUrl).append("' alt='Avatar' style='width:50px;height50px'/>") //TODO: fjern style herfra, problemer å kalle fra css
                .append("<div><p>").append(login).append("</p>")
                .append("<p>Message: ").append(message).append("</p>")
                .append("<p>Date: ").append(date).append("</p></div>")
                .append("</div>");
    }

    // Helper function to escape HTML special characters (ChatGPT)
    public static String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // Convert HTML to Image using Flying Saucer (ChatGPT)
    public static BufferedImage htmlToImage(String htmlContent) {
        ByteArrayInputStream htmlStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        // Load the HTML content into Flying Saucer's XMLResource
        XMLResource xmlResource = XMLResource.load(htmlStream);

        // Create the renderer and set document
        Java2DRenderer renderer = new Java2DRenderer(xmlResource.getDocument(), 600); // Width 600px

        // Render the document to a BufferedImage
        BufferedImage image = renderer.getImage();

        return image;
    }

    // Header settings (ChatGPT)
    public static void setHeaders(HttpHeaders headers, String uniqueTs) {
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.set("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.set("Pragma", "no-cache");
        headers.set("Expires", "0");
        headers.set("Timestamp", uniqueTs);
    }
}
