package org.githubrepositorystats;

import org.githubrepositorystats.Model.Contributor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.CacheControl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/* ChatGPT used as boilerplate code because F Graphics2D */
public class MakeImage {

    private static final Color BACKGROUND_COLOR = new Color(40, 44, 52); // Dark gray background
    private static final Color BORDER_COLOR = new Color(72, 74, 84); // Slightly lighter border color
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // White text color
    private static final Font NAME_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font CONTRIBUTION_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public static ResponseEntity<InputStreamResource> createImageForContributor(List<Contributor> contributors) throws IOException {
        int width = 600;
        int height = 100 + 80 * contributors.size(); // Adjust height based on number of contributors
        int avatarSize = 60;
        int padding = 20;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Enable anti-aliasing for text and graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // Draw background
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, width, height);

        // Draw border
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(4)); // Border thickness
        g2d.drawRect(0, 0, width - 1, height - 1);

        // Draw each contributor
        int y = padding;
        for (Contributor contributor : contributors) {
            // Draw avatar
            BufferedImage avatarImage = ImageIO.read(new URL(contributor.getAvatarUrl()));
            BufferedImage roundedAvatar = createRoundedAvatar(avatarImage, avatarSize);
            g2d.drawImage(roundedAvatar, padding, y, avatarSize, avatarSize, null);

            // Draw contributor name
            g2d.setColor(TEXT_COLOR);
            g2d.setFont(NAME_FONT);
            g2d.drawString(contributor.getLogin(), avatarSize + 2 * padding, y + avatarSize / 2);

            // Draw contribution count
            g2d.setFont(CONTRIBUTION_FONT);
            g2d.drawString("Contributions: " + contributor.getContributions(), avatarSize + 2 * padding, y + avatarSize / 2 + 20);

            y += avatarSize + padding;
        }

        g2d.dispose();

        // Convert BufferedImage to InputStreamResource
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        // Set HTTP headers to prevent caching
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(baos.size());
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());  // Prevents caching
        headers.setPragma("no-cache");  // HTTP/1.0 backward compatibility
        headers.setExpires(0);  // Ensures no caching by setting expiration to past time

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

    public static ResponseEntity<InputStreamResource> createImageForCommit(List<Contributor> contributors) throws IOException {

        return null;
    }
    private static BufferedImage createRoundedAvatar(BufferedImage avatarImage, int size) {
        BufferedImage roundedAvatar = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = roundedAvatar.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a rounded image
        g2d.setClip(new RoundRectangle2D.Double(0, 0, size, size, size / 2, size / 2));
        g2d.drawImage(avatarImage, 0, 0, size, size, null);

        g2d.dispose();
        return roundedAvatar;
    }
}
