package org.githubrepositorystats.ContAndServ;

import org.githubrepositorystats.Model.Commit;
import org.githubrepositorystats.Model.Contributor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.Java2DRenderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ImageService {

    // TODO: faktisk bruk disse, fikse border radius på bilde, legg en linje mellom de?
    private final static String BACKGROUND_COLOR = "rgb(40, 44, 52))";
    private final static String TEXT_COLOR = "rgb(255, 255, 255)";

    // Helper method to generate HTML for contributors
    public static String generateHtmlForContributors(List<Contributor> contributors) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head><meta charset='UTF-8'/><style>")
                .append(".container { width: 600px; background-color: rgb(40, 44, 52); color: white; border-radius: 5%; }")
                .append(".contributor { margin-bottom: 10px; display: flex; align-items: center; padding-left: 5%; }")
                .append(".contributor img { width: 50px; height: 50px; margin-right: 10px; }")
                .append("</style></head>");
        html.append("<body><div class='container'><h1>Contributors</h1>");

        for (Contributor contributor : contributors) {
            html.append("<div class='contributor'>")
                    .append("<img src='").append(contributor.getAvatarUrl()).append("' alt='Avatar' />")
                    .append("<div><p>").append(contributor.getLogin()).append("</p>")
                    .append("<p>Contributions: ").append(contributor.getContributions()).append("</p></div>")
                    .append("</div>");
        }

        html.append("</div></body></html>");
        return html.toString();
    }

    // Helper method to generate HTML for contributors
    public static String generateHtmlForCommits(List<Commit> commits) {
        return null;
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

    // Header settings
    public static void setHeaders(HttpHeaders headers, String uniqueTs) {
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.set("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.set("Pragma", "no-cache");
        headers.set("Expires", "0");
        headers.set("Timestamp", uniqueTs);
    }
}
