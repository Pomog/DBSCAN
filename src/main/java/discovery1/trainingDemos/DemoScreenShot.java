package discovery1.trainingDemos;

import discovery1.GetScreenArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DemoScreenShot {

    public static void main(String[] args) throws Exception {
        Point[] corners = new Point[2];
        String fileName = "screenshot";

        GetScreenArea cornerPoint = new GetScreenArea();

        for (int i = 0; i < corners.length; i++ ) {
            cornerPoint.setConnerToNull();
            cornerPoint.getPoint();
            while (cornerPoint.getConner() == null) {
                // wait for the point to be set
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            corners[i] = cornerPoint.getConner();
            System.out.println("Please select the next corner...");
        }
        System.out.println(corners[0]);
        System.out.println(corners[1]);

        int width = (int) (corners[1].getX() - corners[0].getX());
        int height = (int) (corners[0].getY() - corners[1].getY());
        System.out.println(width);
        System.out.println(height);
        Rectangle screenRectangle = new Rectangle((int) corners[0].getX(), (int) corners[1].getY(), width, height);
        Robot robot = new Robot();

        try {
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            File file = new File("D:\\" + fileName + ".png");
            ImageIO.write(image, "png", file);
            System.out.println("Screenshot taken successfully and saved at D:");
        } catch (Exception e) {
            System.out.println("Error taking screenshot: " + e.getMessage());
        }
    }
}