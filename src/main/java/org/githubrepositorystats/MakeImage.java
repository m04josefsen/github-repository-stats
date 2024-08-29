package org.githubrepositorystats;

import org.githubrepositorystats.Model.Contributor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MakeImage {

    /* ChatGPT used as boilerplate code */
    public static byte[] createImage(List<Contributor> contributors) throws IOException {
        int width = 600;
        int height = 100 + contributors.size() * 60;  // Dynamic height based on the number of contributors
        int avatarSize = 50;
        int padding = 20;
        int textXOffset = padding + avatarSize + 20;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Enable anti-aliasing for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background with gradient
        GradientPaint gradientPaint = new GradientPaint(0, 0, Color.decode("#f5f7fa"), 0, height, Color.decode("#c3cfe2"));
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, width, height);

        // Rounded rectangle as a card
        g2d.setColor(Color.white);
        g2d.fill(new RoundRectangle2D.Double(20, 20, width - 40, height - 40, 30, 30));

        // Set font styles
        Font titleFont = new Font("Roboto", Font.BOLD, 24);
        Font textFont = new Font("Roboto", Font.PLAIN, 18);
        g2d.setFont(titleFont);
        g2d.setColor(Color.decode("#333333"));

        // Draw title
        g2d.drawString("Top Contributors", padding, 60);

        // Loop through contributors and draw each one
        g2d.setFont(textFont);
        int yOffset = 100;
        for (Contributor contributor : contributors) {
            // Draw contributor's avatar
            BufferedImage avatar = ImageIO.read(new URL(contributor.getAvatarUrl())); // Fetch the avatar from URL
            g2d.drawImage(avatar, padding, yOffset - avatarSize / 2, avatarSize, avatarSize, null);

            // Draw contributor's login and contributions
            g2d.drawString(contributor.getLogin(), textXOffset, yOffset);
            g2d.drawString("Contributions: " + contributor.getContributions(), textXOffset, yOffset + 20);

            yOffset += 60;  // Move down for the next contributor
        }

        // Clean up
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();

        return baos.toByteArray();
    }
}
