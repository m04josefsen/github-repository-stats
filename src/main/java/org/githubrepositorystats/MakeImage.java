package org.githubrepositorystats;

import org.githubrepositorystats.Model.Commit;
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
import java.text.SimpleDateFormat;
import java.util.List;

/* ChatGPT used as boilerplate code because F Graphics2D */
public class MakeImage {

    private static final Color BACKGROUND_COLOR = new Color(40, 44, 52); // Dark gray background
    private static final Color BORDER_COLOR = new Color(72, 74, 84); // Slightly lighter border color
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // White text color
    private static final Font NAME_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font CONTRIBUTION_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Font DATE_FONT = new Font("Segoe UI", Font.PLAIN, 12);

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

    public static ResponseEntity<InputStreamResource> createImageForCommits(List<Commit> commits) throws IOException {
        int width = 600;
        int avatarSize = 60;
        int padding = 20;
        int labelHeight = 20;
        int textMaxWidth = width - (avatarSize + 3 * padding); // Max width for text before wrapping

        // Calculate dynamic height based on the commits
        int height = padding; // Start with padding at the top

        // Calculate height for first commit
        Commit firstCommit = commits.get(commits.size() - 1);
        height += labelHeight + avatarSize + padding; // Add label and avatar size
        int firstCommitTextHeight = calculateWrappedTextHeight(firstCommit.getMessage(), CONTRIBUTION_FONT, textMaxWidth);
        height += firstCommitTextHeight + 40; // Add space for wrapped text and date

        // Calculate height for latest commit
        Commit latestCommit = commits.get(0);
        height += labelHeight + avatarSize + padding; // Add label and avatar size
        int latestCommitTextHeight = calculateWrappedTextHeight(latestCommit.getMessage(), CONTRIBUTION_FONT, textMaxWidth);
        height += latestCommitTextHeight + 40; // Add space for wrapped text and date

        // Create a dynamic BufferedImage with the calculated height
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Enable anti-aliasing for text and graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // Draw dark background
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, width, height);

        // Draw border
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(4)); // Border thickness
        g2d.drawRect(0, 0, width - 1, height - 1);

        // Draw the first commit
        int y = padding;
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(NAME_FONT);
        g2d.drawString("First Commit", padding, y + labelHeight);

        if (firstCommit.getAvatarUrl() != null && !firstCommit.getAvatarUrl().isEmpty()) {
            BufferedImage avatarImage = ImageIO.read(new URL(firstCommit.getAvatarUrl()));
            BufferedImage roundedAvatar = createRoundedAvatar(avatarImage, avatarSize);
            g2d.drawImage(roundedAvatar, padding, y + labelHeight + padding / 2, avatarSize, avatarSize, null);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(NAME_FONT);
        g2d.drawString(firstCommit.getLogin(), avatarSize + 2 * padding, y + labelHeight + avatarSize / 2);

        g2d.setFont(CONTRIBUTION_FONT);
        int textHeight = drawWrappedText(g2d, "Message: " + firstCommit.getMessage(), avatarSize + 2 * padding, y + labelHeight + avatarSize / 2 + 20, textMaxWidth);

        g2d.setFont(DATE_FONT);
        g2d.drawString("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(firstCommit.getDate()), avatarSize + 2 * padding, textHeight + 20);

        // Draw the latest commit
        y += avatarSize + firstCommitTextHeight + 3 * padding + labelHeight;
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(NAME_FONT);
        g2d.drawString("Latest Commit", padding, y + labelHeight);

        if (latestCommit.getAvatarUrl() != null && !latestCommit.getAvatarUrl().isEmpty()) {
            BufferedImage avatarImage = ImageIO.read(new URL(latestCommit.getAvatarUrl()));
            BufferedImage roundedAvatar = createRoundedAvatar(avatarImage, avatarSize);
            g2d.drawImage(roundedAvatar, padding, y + labelHeight + padding / 2, avatarSize, avatarSize, null);
        }

        g2d.setColor(TEXT_COLOR);
        g2d.setFont(NAME_FONT);
        g2d.drawString(latestCommit.getLogin(), avatarSize + 2 * padding, y + labelHeight + avatarSize / 2);

        g2d.setFont(CONTRIBUTION_FONT);
        textHeight = drawWrappedText(g2d, "Message: " + latestCommit.getMessage(), avatarSize + 2 * padding, y + labelHeight + avatarSize / 2 + 20, textMaxWidth);

        g2d.setFont(DATE_FONT);
        g2d.drawString("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestCommit.getDate()), avatarSize + 2 * padding, textHeight + 20);

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

    private static int drawWrappedText(Graphics2D g2d, String text, int x, int y, int maxWidth) {
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int currentY = y;

        for (String word : words) {
            String testLine = line + word + " ";
            int testLineWidth = fontMetrics.stringWidth(testLine);

            // If the line is too wide, draw the current line and reset
            if (testLineWidth > maxWidth) {
                g2d.drawString(line.toString(), x, currentY);
                line = new StringBuilder(word + " ");
                currentY += lineHeight;
            } else {
                line.append(word).append(" ");
            }
        }

        // Draw the remaining line
        if (line.length() > 0) {
            g2d.drawString(line.toString(), x, currentY);
        }

        // Return the updated Y position after the last line is drawn
        return currentY + lineHeight;
    }

    private static int calculateWrappedTextHeight(String text, Font font, int maxWidth) {
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dummyImage.createGraphics();
        g2d.setFont(font);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int totalHeight = lineHeight; // Start with one line height

        for (String word : words) {
            String testLine = line + word + " ";
            int testLineWidth = fontMetrics.stringWidth(testLine);

            if (testLineWidth > maxWidth) {
                totalHeight += lineHeight;
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }

        g2d.dispose();

        return totalHeight;
    }
}
