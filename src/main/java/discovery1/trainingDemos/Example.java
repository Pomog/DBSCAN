package discovery1.trainingDemos;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

public class Example {
    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1.0, 2.0));
        points.add(new Point(3.0, 4.0));
        points.add(new Point(5.0, 6.0));

        double[][] processedPointsArray = pointsTo2DArray(points);

        System.out.println("The processed points array: \n");
        for (double[] point : processedPointsArray) {
            System.out.println("x: " + point[0] + ", y: " + point[1]);
        }
    }

    public static double[][] pointsTo2DArray(List<Point> points) {
        return points.stream()
                .map(p -> new double[]{p.x, p.y})
                .toArray(double[][]::new);
    }
}