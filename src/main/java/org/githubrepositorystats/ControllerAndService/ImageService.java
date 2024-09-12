package org.githubrepositorystats.ControllerAndService;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.Java2DRenderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ImageService {

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
