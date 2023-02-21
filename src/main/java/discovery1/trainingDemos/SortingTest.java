package discovery1.trainingDemos;


import discovery1.Image;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

public class SortingTest {
    public static void main(String[] args) {

        Image image = new Image();

        Point start = new Point(68.0, 179.0);
        List<Point> pointsTest = new ArrayList<>();
        pointsTest.add(new Point(149.0, 107.0));
        pointsTest.add(new Point(68.0, 210.0));
        pointsTest.add(new Point(235.0, 119.0));

        System.out.println(pointsTest);

        pointsTest.sort((p1, p2) -> {
            double angle1 = Math.atan2(p1.y - start.y, p1.x - start.x);
            double angle2 = Math.atan2(p2.y - start.y, p2.x - start.x);
            if (angle1 == angle2) {
                double dist1 = distance(start, p1);
                double dist2 = distance(start, p2);
                return Double.compare(dist2, dist1);
            } else {
                return Double.compare(angle2, angle1);
            }
        });

        System.out.println(pointsTest);

        int stepCalculator = 1;
        List<Point> step = new ArrayList<>();
        for (Point point : pointsTest) {
            double angleA = Math.atan2(point.y - start.y, point.x - start.x);
            System.out.println("atan2 : " + angleA);

            step.add(point);
            String stepScreenShot = "D:/step" + stepCalculator + ".png";
            image.listOfPoinsToImage(step, "D:/screenshot-hull.png", stepScreenShot);
            stepCalculator++;
        }


    }
    private static double distance (Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }



}
