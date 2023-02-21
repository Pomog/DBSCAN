package discovery1.trainingDemos;

import discovery1.ConvexHull;
import discovery1.DBScan;
import discovery1.Image;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

public class HullTest {
    public static void main(String[] args) {
        String initialScreenShotPath = "D:/screenshot-hull.png";
        String processedScreenShotPath = "D:/screenshot_bright-hull.png"; // for information
        String convertedToBinaryScreenShotPath = "D:/screenshot_bright_binary-hull.png";
        String imageFromPointsPath = "D:/screenshot_points-hull.png";
        String FINImage = "D:/hull_DEV.png";
        Scalar whiteColor = new Scalar(255, 0, 0);

      // TakeScreenShot screenShot = new TakeScreenShot("D:/screenshot-hull.png");
      // screenShot.capture();

        Image imageProcessing = new Image();
        imageProcessing.processingScreenShotToBinary(initialScreenShotPath, convertedToBinaryScreenShotPath);
        Mat binaryImage = Imgcodecs.imread(convertedToBinaryScreenShotPath);
        List<Point> pointsBinaryImage = imageProcessing.pixelCoordinatesToList(binaryImage);
        imageProcessing.createImageFromPointList(pointsBinaryImage, whiteColor, imageFromPointsPath);
        Mat processedImage = Imgcodecs.imread(imageFromPointsPath);
        List<Point> pointsProcessedImage = imageProcessing.pixelCoordinatesToList(processedImage);
        double[][] processedPointsArray = imageProcessing.pointsTo2DArray(pointsProcessedImage);

        DBScan clustersToPNG = new DBScan(initialScreenShotPath);
        clustersToPNG.start(processedPointsArray);

        System.out.println(clustersToPNG.getPointsAfterClusterizationMap().keySet());

        List<Point> cluster = clustersToPNG.getPointsAfterClusterizationMap().get("Cluster: 0 Cluster");
        //System.out.println(cluster);

        Image image = new Image();
        image.listOfPoinsToImage(cluster, initialScreenShotPath, FINImage);

        ConvexHull hull = new ConvexHull(cluster);
        hull.removeDuplicatePoints();
        List<Point> duplicatePointsRomoved = hull.getPoints();
        image.listOfPoinsToImage(duplicatePointsRomoved, initialScreenShotPath, "D:/duplicatePointsRemoved.png");

        hull.findP0();
        List<Point> startingPoint = List.of(hull.getStart());
        System.out.println("P0 : " + hull.getStart());
        image.listOfPoinsToImage(startingPoint, initialScreenShotPath, "D:/startingPoint.png");

        hull.reducePointsByDensity();
        List<Point> reducedPoints = hull.getReducedPoints();
        //System.out.println("reducedPoints : " + reducedPoints);
        image.listOfPoinsToImage(reducedPoints, initialScreenShotPath, "D:/reducedPoints.png");

       /*
        reducedPoints.add(new Point(68.0, 195.0));
        reducedPoints.add(new Point(68.0, 210.0));
        System.out.println("Added Points in Line");
        System.out.println("after addition : " + hull.getReducedPoints());
        image.listOfPoinsToImage(reducedPoints, initialScreenShotPath, "D:/Added Points in Line.png");

        */

        hull.findP0(reducedPoints);
        System.out.println("P0 override after reducing : " + hull.getStart());

        hull.removeCollinearPoints();
        List<Point> removedInnerPoints = hull.getRemoveCollinearPoints();
        System.out.println("removedInnerPoints : " + removedInnerPoints);
        image.listOfPoinsToImage(removedInnerPoints, initialScreenShotPath, "D:/removedInnerPoints.png");

        hull.sortThePointsByAngle();
       // hull.sortByPolarAngle(reducedPoints);
        List<Point> sortedPoints = hull.getSortedPoints();
        System.out.println("sortedPoints : " + hull.getSortedPoints());
        image.listOfPoinsToImage(sortedPoints, initialScreenShotPath, "D:/sortedPoints.png");

        hull.findP0(sortedPoints);
        System.out.println("P0 override after sorting : " + hull.getStart());

/*
        int stepCalculator = 1;
        List<Point> step = new ArrayList<>();
        for (Point point : sortedPoints) {
            double angleA = Math.atan2(point.y - hull.getStart().y, point.x - hull.getStart().x);
            System.out.println("atan2 : " + angleA);

            step.add(point);
            String stepScreenShot = "D:/step" + stepCalculator + ".png";
            image.listOfPoinsToImage(step, "D:/screenshot-hull.png", stepScreenShot);
            stepCalculator++;
        }

 */

        System.out.println("calculating Hull");
        List<Point> calculatedHull = hull.calculateConvexHull(sortedPoints);
        System.out.println("calculatedHull : " + calculatedHull);
        image.listOfPoinsToImage(calculatedHull, initialScreenShotPath, "D:/calculatedHull.png");



       /*
        hull.getConvexHull();
        List<Point> hullPoints = hull.getHull();
        image.listOfPoinsToImage(hullPoints, initialScreenShotPath, "D:/HullPoints.png");

        */




    }
}
