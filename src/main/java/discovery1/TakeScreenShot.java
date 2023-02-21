package discovery1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TakeScreenShot {
    private Point[] corners = new Point[3];
    private String disk = "D:/";
    private String fileName = disk + "screenshot.png";
    private Point submit;

    public TakeScreenShot() {
    }
    public TakeScreenShot(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }

    public Point getLeftTop(){
        return new Point((int) corners[0].getX(), (int) corners[1].getY());
    }

    public Point getSubmitButtonCoordinates(){
        return new Point((int) corners[2].getX(), (int) corners[2].getY());
    }

    public void initialCapture() {
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
            //System.out.println("Please select the next corner...");
        }



//ТУТ РАЗДЕЛИТЬ НА 2 МЕТОДА И В ДЕМО ПРОВЕРЯТЬ ПУСТЫЕ Point[] corners ИЛИ НЕТ

        int width = (int) (corners[1].getX() - corners[0].getX());
        int height = (int) (corners[0].getY() - corners[1].getY());
        System.out.println("width " + width);
        System.out.println("height " + height);
        Rectangle screenRectangle = new Rectangle((int) corners[0].getX(), (int) corners[1].getY(), width, height);

        try {
            Robot robot = new Robot();
            try {
                BufferedImage image = robot.createScreenCapture(screenRectangle);
                File file = new File(fileName);
                ImageIO.write(image, "png", file);
                System.out.println("Screenshot taken successfully and saved > " + fileName);
            } catch (Exception e) {
                System.out.println("Error taking screenshot: " + e.getMessage());
            }
        }
        catch (AWTException e) {
            System.out.println("An error occurred while creating the robot object: " + e.getMessage());
        }
    }

    public void capture() {
        int width = (int) (corners[1].getX() - corners[0].getX());
        int height = (int) (corners[0].getY() - corners[1].getY());
        System.out.println("width " + width);
        System.out.println("height " + height);
        Rectangle screenRectangle = new Rectangle((int) corners[0].getX(), (int) corners[1].getY(), width, height);

        try {
            Robot robot = new Robot();
            try {
                BufferedImage image = robot.createScreenCapture(screenRectangle);
                File file = new File(fileName);
                ImageIO.write(image, "png", file);
                System.out.println("Screenshot taken successfully and saved > " + fileName);
            } catch (Exception e) {
                System.out.println("Error taking screenshot: " + e.getMessage());
            }
        }
        catch (AWTException e) {
            System.out.println("An error occurred while creating the robot object: " + e.getMessage());
        }
    }
}