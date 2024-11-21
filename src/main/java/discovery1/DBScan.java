package discovery1;

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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;
import java.util.stream.Collectors;

public class DBScan {
    private int radius = 15;
    private int epsilon = 30;
    private int minpts = 10;
    private String clustersImagePath = "D:/clusterization";
    private Clustering<Model> result;
    private Relation<NumberVector> rel;
    Map<String, List<Point>> pointsAfterClusterizationMap = new HashMap<>();

    public Map<String, List<Point>> getPointsAfterClusterizationMap() {
        return pointsAfterClusterizationMap;
    }
    private String sampleImagePath = "D:/screenshot.png";
    public DBScan(String sampleImagePath) {
        this.sampleImagePath = sampleImagePath;
    }

    public void start(double[][] processedPointsArray) {
        //System.out.println("Initial Array \n" + Arrays.deepToString(processedPointsArray));
        //reduce points in double[][] processedPointsArray by radius.The initial array is too large and slow processing
        double[][] reducedPointsArray = DBScan.reducePoints(processedPointsArray, radius);
        //System.out.println("Reduced Array \n" +Arrays.deepToString(reducedPointsArray));
        //Create connection
        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(reducedPointsArray);
        // Create a new database
        Database db = new StaticArrayDatabase(dbc, null);
        db.initialize();
        // Get the relation of points in the database
        rel = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);
        // Set the parameters for DBSCAN
        DBSCAN<NumberVector> dbscan = new DBSCAN<>(EuclideanDistanceFunction.STATIC, epsilon, minpts); //magic
        // Run DBSCAN on the relation
        result = dbscan.run(rel);
        clustersToPNG(sampleImagePath);
    }
    public void clustersToPNG (String sampleImagePath) {
        Mat image = Imgcodecs.imread(sampleImagePath);
        Mat clustersPNG = new Mat(image.size(), CvType.CV_8UC3);
        int i = 0;
        for (Cluster<Model> cluster : result.getAllClusters()) {
            String clusterName = "Cluster: " + i++ + " " + cluster.getNameAutomatic();
            System.out.println(clusterName);
            if (!Objects.equals(cluster.getNameAutomatic(), "Null")) {
                List<Point> pointsAfterClusterization = new ArrayList<>();
                for (DBIDIter it = cluster.getIDs().iter(); it.valid(); it.advance()) {
                    NumberVector point = rel.get(it);
                    double x = point.doubleValue(0);
                    double y = point.doubleValue(1);
                    Point p = new Point(x, y);
                    pointsAfterClusterization.add(p);
                }
                Scalar randomColor = getRandomColor();
                clustersPNG.setTo(new Scalar(0, 0, 0));
                pointsAfterClusterizationMap.put(clusterName, pointsAfterClusterization);
                pointsAfterClusterization.forEach(p ->
                        Imgproc.circle(clustersPNG, p, 1, randomColor, -1));
                Imgcodecs.imwrite(clustersImagePath + i + ".png", clustersPNG);
            }
        }
    }
    public  Map<String, List<Point>> getClusters() {
        return pointsAfterClusterizationMap.entrySet()
                .stream()
                .filter(entry -> !entry.getKey().contains("Noise"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    private static Scalar getRandomColor () {
        Random rand = new Random();
        int r = (150);
        int g = rand.nextInt(205) + 50;
        int b = rand.nextInt(205) + 50;
        return new Scalar(r, g, b);
    }
    private static double distance(double[] point1, double[] point2) {
        double xDiff = point1[0] - point2[0];
        double yDiff = point1[1] - point2[1];
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
    private static double[][] reducePoints(double[][] processedPointsArray, int radius) {
        List<double[]> result = new ArrayList<>();
        for (double[] point : processedPointsArray) {
            boolean addPoint = true;
            for (double[] existingPoint : result) {
                if (point == existingPoint) {
                    continue;
                }
                if (distance(existingPoint, point) < radius) {
                    addPoint = false;
                    break;
                }
            }
            if (addPoint) {
                result.add(point);
            }
        }
        return result.toArray(new double[0][]);
    }
}
