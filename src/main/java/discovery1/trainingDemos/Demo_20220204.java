package discovery1.trainingDemos;

import discovery1.ConvexHull;
import discovery1.DBScan;
import discovery1.Image;
import discovery1.TakeScreenShot;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

public class Demo_20220204 {

    public static void main(String[] args) {

         String initialScreenShotPath = "D:/screenshot.png";
         String processedScreenShotPath = "D:/screenshot_bright.png"; // for information
         String convertedToBinaryScreenShotPath = "D:/screenshot_bright_binary.png";
         String imageFromPointsPath = "D:/screenshot_points.png";
         Scalar whiteColor = new Scalar(255, 0, 0);

       // TakeScreenShot screenShot = new TakeScreenShot();
       // screenShot.capture();

        //convert screenShot to the Binary Image and write coordinates of the dots to double[][] ArrayList
        Image imageProcessing = new Image();
        imageProcessing.processingScreenShotToBinary(initialScreenShotPath, convertedToBinaryScreenShotPath);
        Mat binaryImage = Imgcodecs.imread(convertedToBinaryScreenShotPath);
        List<Point> pointsBinaryImage = imageProcessing.pixelCoordinatesToList(binaryImage);
        imageProcessing.createImageFromPointList(pointsBinaryImage, whiteColor, imageFromPointsPath);
        Mat processedImage = Imgcodecs.imread(imageFromPointsPath);
        List<Point> pointsProcessedImage = imageProcessing.pixelCoordinatesToList(processedImage);
        double[][] processedPointsArray = imageProcessing.pointsTo2DArray(pointsProcessedImage);

        //implementing DBSCAN algorithm. ELKI library
        //use method List<Point> getPointsAfterClusterization() as starting base to calculate Hull
        DBScan clustersToPNG = new DBScan(initialScreenShotPath);
        clustersToPNG.start(processedPointsArray);

       // Convex Hull
        System.out.println(clustersToPNG.getPointsAfterClusterizationMap().keySet());

        Image image = new Image();

        List<Point> cluster = clustersToPNG.getPointsAfterClusterizationMap().get("Cluster: 1 Cluster");
        ConvexHull hull = new ConvexHull(cluster);

        image.listOfPoinsToImage(cluster, initialScreenShotPath, "D:/hull1.png");


        hull.getConvexHull();
        List<Point> processedCluster = hull.getRemoveCollinearPoints();

        List<Point> clusterHull = hull.calculateConvexHull(processedCluster);
        image.listOfPoinsToImage(clusterHull, initialScreenShotPath, "D:/clusterHull.png");



    }
}
