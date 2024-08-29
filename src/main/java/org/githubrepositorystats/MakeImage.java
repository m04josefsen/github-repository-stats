package org.githubrepositorystats;

import org.githubrepositorystats.Model.Contributor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class MakeImage {
    public static byte[] createImage(List<Contributor> contributorlist) throws IOException {
        int width = 400;
        int height = 250;
        int circleWidth = height;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.green);
        g2d.fillOval(150, 0, circleWidth, height);
        g2d.setColor(Color.blue);
        g2d.fillOval(75, 0, circleWidth, height);
        g2d.setColor(Color.yellow);
        g2d.fillOval(0, 0, circleWidth, height);

        g2d.setColor(Color.black);

        String print = "";
        for(Contributor c : contributorlist) {
            print += "Login: " + c.getLogin() + "\n";
        }
        g2d.drawString(print, 50, 125);

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();

        return baos.toByteArray();
    }
}
