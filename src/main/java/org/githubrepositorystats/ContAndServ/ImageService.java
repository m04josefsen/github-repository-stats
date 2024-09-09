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

        // TODO: sliter med å få styling pga dynamisk laget?
        for (Contributor c : contributors) {
            html.append("<div class='contributor'>")
                    .append("<img src='").append(c.getAvatarUrl()).append("' alt='Avatar' />")
                    .append("<div><p>").append(c.getLogin()).append("</p>")
                    .append("<p>Contributions: ").append(c.getContributions()).append("</p></div>")
                    .append("</div>");
        }

        html.append("</div></body>");
        html.append("</html>");
        return html.toString();
    }

    // Helper method to generate HTML for contributors
    public static String generateHtmlForCommits(List<Commit> commits) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head><meta charset='UTF-8'/></head>");
        html.append("<body><div class='container'><h1>Commits</h1>");

        for(Commit c : commits) {
            html.append("<div class='commit'>")
                    .append("<img src='").append(c.getAvatarUrl()).append("' alt='Avatar' />")
                    .append("<div><p>").append(c.getLogin()).append("</p>")
                    .append("<p>Message: ").append(c.getMessage()).append("</p>")
                    .append("<p>Date: ").append(c.getDate()).append("</p></div>")
                    .append("</div>");
        }

        html.append("</div></body></html>");
        return html.toString();
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
