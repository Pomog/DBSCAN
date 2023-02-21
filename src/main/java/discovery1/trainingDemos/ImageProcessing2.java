package discovery1.trainingDemos;

import de.lmu.ifi.dbs.elki.algorithm.clustering.DBSCAN;
import de.lmu.ifi.dbs.elki.data.Cluster;
import de.lmu.ifi.dbs.elki.data.Clustering;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.model.Model;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.datasource.DatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageProcessing2 {
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
            //System.out.println(Arrays.deepToString(pointsArray));
        }

        // Create a database connection
        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(pointsArray);

        // Create a new database
        Database db = new StaticArrayDatabase(dbc, null);
        db.initialize();

        // Get the relation of points in the database
        Relation<NumberVector> rel = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);

        // Set the parameters for DBSCAN
        DBSCAN<NumberVector> dbscan = new DBSCAN<>(EuclideanDistanceFunction.STATIC, 35, 100);

        // Run DBSCAN on the relation
        Clustering<Model> result = dbscan.run(rel);


        Mat clusterizationPNG = new Mat(image.size(), CvType.CV_8UC3);

        List<Point> pointsAfterClusterization = new ArrayList<>();
        int i = 0;
        for (Cluster<Model> cluster : result.getAllClusters()) {
            System.out.println("Cluster: " + i++ + " " + cluster.getNameAutomatic() );
            if (cluster.getNameAutomatic() != "Noise") {
                for (DBIDIter it = cluster.getIDs().iter(); it.valid(); it.advance()) {
                    NumberVector point = rel.get(it);
                    double x = point.doubleValue(0);
                    double y = point.doubleValue(1);
                    Point p = new Point(x, y);
                    pointsAfterClusterization.add(p);
                }
                Scalar randomColor = getColor();
                clusterizationPNG.setTo(new Scalar(0, 0, 0));
                pointsAfterClusterization.forEach(p ->
                        Imgproc.circle(clusterizationPNG, p, 3, randomColor, -1));
                Imgcodecs.imwrite("D:/clusterization" + i + ".png", clusterizationPNG);
                pointsAfterClusterization.clear();
            }
        }




    }
    static Scalar getColor () {
        Random rand = new Random();
        int r = (150);
        int g = rand.nextInt(205) + 50;
        int b = rand.nextInt(205) + 50;
        return new Scalar(r, g, b);
    }
}
