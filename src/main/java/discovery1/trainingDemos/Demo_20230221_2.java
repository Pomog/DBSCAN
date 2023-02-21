package discovery1.trainingDemos;

import discovery1.*;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

public class Demo_20230221_2 {

    public static void main(String[] args) {
        String convertedToBinaryScreenShotPath = "D:/screenshot_bright_binary.png";
        String imageFromPointsPath = "D:/screenshot_points.png";
        Scalar whiteColor = new Scalar(255, 0, 0);
        String initialScreenshotPath = "D:/screenshot.png";

        TakeScreenShot screenShot = new TakeScreenShot();
        screenShot.initialCapture();

        Image imageProcessing = new Image();
        imageProcessing.processingScreenShotToBinary(initialScreenshotPath, convertedToBinaryScreenShotPath);

        Mat binaryImage = Imgcodecs.imread(convertedToBinaryScreenShotPath);
        List<Point> pointsBinaryImage = imageProcessing.pixelCoordinatesToList(binaryImage);
        imageProcessing.createImageFromPointList(pointsBinaryImage, whiteColor, imageFromPointsPath);

        Mat processedImage = Imgcodecs.imread(imageFromPointsPath);
        List<Point> pointsProcessedImage = imageProcessing.pixelCoordinatesToList(binaryImage);
        double[][] processedPointsArray = imageProcessing.pointsTo2DArray(pointsProcessedImage);

        DBScan dbScan = new DBScan(initialScreenshotPath);
        System.out.println("Starting DBScan");
        dbScan.start(processedPointsArray);

        System.out.println(dbScan.getClusters().keySet());



        ConvexHull hull = new ConvexHull(dbScan.getClusters().get("Cluster: 0 Cluster"));
        hull.getConvexHull();
        imageProcessing.listOfPoinsToImage(hull.getHull(), initialScreenshotPath, "D:/clusterHull.png");
        System.out.println(hull.getHull());

        MouseDriver mouseDriver = new MouseDriver(screenShot.getLeftTop());
        mouseDriver.drawHull(hull.normalizeHullTo10Points( hull.getHull()));

    }
}
