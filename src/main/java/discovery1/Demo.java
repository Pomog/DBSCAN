package discovery1;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Demo {

    public static void main(String[] args) {
        int repetitions = 3;
        String convertedToBinaryScreenShotPath = "D:/screenshot_bright_binary.png";
        String imageFromPointsPath = "D:/screenshot_points.png";
        Scalar whiteColor = new Scalar(255, 0, 0);
        String initialScreenshotPath = "D:/screenshot.png";

        TakeScreenShot screenShot = new TakeScreenShot();
        screenShot.initialCapture();
        MouseDriver mouseDriver = new MouseDriver(screenShot.getLeftTop());

        for (int i = 0; i < repetitions; i++) {
            pause(2);
            if (i > 0) {screenShot.capture();}
            Image imageProcessing = new Image();
            imageProcessing.processingScreenShotToBinary(initialScreenshotPath, convertedToBinaryScreenShotPath);

            Mat binaryImage = Imgcodecs.imread(convertedToBinaryScreenShotPath);
            List<Point> pointsBinaryImage = imageProcessing.pixelCoordinatesToList(binaryImage);
            imageProcessing.createImageFromPointList(pointsBinaryImage, whiteColor, imageFromPointsPath);

            Mat processedImage = Imgcodecs.imread(imageFromPointsPath);
            List<Point> pointsProcessedImage = imageProcessing.pixelCoordinatesToList(processedImage);
            double[][] processedPointsArray = imageProcessing.pointsTo2DArray(pointsProcessedImage);

            DBScan dbScan = new DBScan(initialScreenshotPath);
            System.out.println("Starting DBScan");
            dbScan.start(processedPointsArray);

            System.out.println(dbScan.getClusters().keySet());

            for (String clusterName : dbScan.getClusters().keySet()) {
                ConvexHull hull = new ConvexHull(dbScan.getClusters().get(clusterName));
                hull.getConvexHull();
                imageProcessing.listOfPoinsToImage(hull.getHull(), initialScreenshotPath, "D:/clusterHull.png");
                System.out.println(hull.getHull());

                pause(2);
                mouseDriver.drawHull(hull.normalizeHullTo10Points(hull.getHull()));
            }

            pause(2);
            mouseDriver.mouseClick(screenShot.getSubmitButtonCoordinates());
            pause(2);
            mouseDriver.mouseClick(screenShot.getSubmitButtonCoordinates());
            pause(3);
            mouseDriver.mouseClick(screenShot.getSubmitButtonCoordinates());
        }
    }

    private static void pause(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
