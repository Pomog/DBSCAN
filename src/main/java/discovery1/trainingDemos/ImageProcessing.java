package discovery1.trainingDemos;

import de.lmu.ifi.dbs.elki.algorithm.clustering.DBSCAN;
import de.lmu.ifi.dbs.elki.data.Cluster;
import de.lmu.ifi.dbs.elki.data.Clustering;
import de.lmu.ifi.dbs.elki.data.DoubleVector;
import de.lmu.ifi.dbs.elki.data.model.Model;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessing {
    public static void main(String[] args) {
        // Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Read image file
        Mat image = Imgcodecs.imread("D:/screenshot.png", Imgcodecs.IMREAD_GRAYSCALE);

        Mat brightenedImage = new Mat(image.size(), image.type());
        Core.addWeighted(image, 1.5, brightenedImage, 0.0, 0, brightenedImage);
        Imgcodecs.imwrite("D:/screenshot_bright.png", brightenedImage);

        // Check if image is loaded properly
        if(image.empty()) {
            System.out.println("Error: Image not loaded properly.");
            return;
        }

        // Apply threshold to the image to convert dots to white pixels
        Imgproc.threshold(brightenedImage, brightenedImage, 50, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite("D:/screenshot_bright_binary.png", brightenedImage);

        // Find the coordinates of all white pixels in the image
        List<Point> points = new ArrayList<>();
        for (int y = 0; y < brightenedImage.rows(); y++) {
            for (int x = 0; x < brightenedImage.cols(); x++) {
                double[] color = brightenedImage.get(y, x);
                if (color[0] == 255.0) {
                    points.add(new Point(x, y));
                }
            }
        }
       // points.forEach(System.out::println);

        for (Point point : points) {
            Imgproc.circle(brightenedImage, point, 1, new Scalar(255, 0, 0), -1);
        }
        Imgcodecs.imwrite("D:/screenshot_points.png", brightenedImage);

        Mat imageFromPoints = Imgcodecs.imread("D:/screenshot_points.png", Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.threshold(imageFromPoints, imageFromPoints, 50, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite("D:/screenshot_points2.png", brightenedImage);

        List<Point> points2 = new ArrayList<>();
        for (int y = 0; y < brightenedImage.rows(); y++) {
            for (int x = 0; x < brightenedImage.cols(); x++) {
                double[] color = brightenedImage.get(y, x);
                if (color[0] == 255.0) {
                   // System.out.println("+");
                    points2.add(new Point(x, y));
                }
            }
        }
        //System.out.println("*****************************************");
       // points2.forEach(System.out::println);

        double[][] pointsArray = new double[points2.size()][2];
        for (int i = 0; i < points2.size(); i++) {
            pointsArray[i][0] = points2.get(i).x;
            pointsArray[i][1] = points2.get(i).y;
        }

        // Create a new ELKI database and add the points to it
        Database db = new StaticArrayDatabase(new ArrayAdapterDatabaseConnection(pointsArray), null);
        db.initialize();

        // Create a new DBSCAN algorithm object
        DBSCAN<DoubleVector> dbscan = new DBSCAN<>(EuclideanDistanceFunction.STATIC, 10, 100);

        // Run the DBSCAN algorithm on the database
        Clustering<Model> result = dbscan.run(db);
        // ************* Clustering Finished *************

        System.out.println("\nresult - class: " + result.getClass() +"\n");

        for (Cluster<Model> cluster : result.getAllClusters()) {
            System.out.println("Cluster: " + cluster);
            System.out.println("Cluster class: " + cluster.getClass());
           // System.out.println("Cluster getIDs: " + cluster.getIDs());
            System.out.println("Cluster getModel: " + cluster.getModel());
            System.out.println("Cluster getModel().getClass(): " + cluster.getModel().getClass());
            System.out.println("");
        }

        for (Cluster<Model> cluster : result.getAllClusters()) {
            //cluster.getIDs().forEach(System.out::println);
         //   cluster.getModel().getPrototype().toString();



            }




/*
        // Create a new image to draw the clusters on
                Mat imageWithClusters = new Mat(image.size(), image.type());

        for (Cluster<Model> cluster : result.getAllClusters()) {
            for (DoubleVector point : cluster.getModel().getPrototype().getPoints()) {
                Imgproc.circle(img, new Point(point.get(0), point.get(1)), 1, new Scalar(255, 0, 0), -1);
            }
        }

 */




    }
}
