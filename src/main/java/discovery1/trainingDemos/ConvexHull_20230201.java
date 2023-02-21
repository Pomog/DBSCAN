package discovery1.trainingDemos;

import org.opencv.core.Point;

import java.util.*;
import java.util.stream.Collectors;

//  Graham Scan
public class ConvexHull_20230201 {
    private Point start;
    private List<Point> sortedPoints;
    private List<Point> reducedPoints;
    private final Point[] previous = {null};
    private List <Point> removeCollinearPoints;
    public List<Point> getRemoveCollinearPoints() {
        return removeCollinearPoints;
    }
    public List<Point> getReducedPoints() {
        return reducedPoints;
    }
    int maxDistance = 25;
    public List<Point> getSortedPoints() {
        return sortedPoints;
    }
    public Point getStart() {
        return start;
    }
    private List<Point> hull;
    private List<Point> points;
    public List<Point> getPoints() {
        return points;
    }
    public List<Point> getHull() {
        return hull;
    }
    public ConvexHull_20230201(List<Point> points) {
        this.points = points;
    }

 // NOT WORKED
    public void getConvexHull() {

        removeDuplicatePoints ();
        findP0 ();
        reducePointsByDensity();
        sortThePointsByAngle();
        findP0 (sortedPoints);
        removeCollinearPoints();

    }
    // worked
    public void removeDuplicatePoints (){
        Set<Point> uniquePoints = new HashSet<>(points);
        points.clear();
        points.addAll(uniquePoints);
        //if not sorted, hard to remove nearby points by a stream :)
        points.sort((a, b) -> {
            if (a.x != b.x) {
                return Double.compare(a.x, b.x);
            } else {
                return Double.compare(a.y, b.y);
            }
        });
    }
    // worked
    public void findP0 (){
        // Find the lowest point, !!!y=0 top of the screen!!! (or the leftmost one in case of tie)
        start = points.stream()
                .reduce((p1, p2) -> p1.y > p2.y || (p1.y == p2.y && p1.x < p2.x) ? p1 : p2)
                .orElse(points.get(0));
    }
    //Overload
    public void findP0 (List<Point> points){
           start = points.stream()
                .reduce((p1, p2) -> p1.y > p2.y || (p1.y == p2.y && p1.x < p2.x) ? p1 : p2)
                .orElse(points.get(0));
    }
    // worked
    public void sortThePointsByAngle() {
        // Sort the points by polar angle with respect to the start point
        final Point startPoint = start;
        sortedPoints = reducedPoints.stream()
                .sorted(pointComparator(startPoint))
                .collect(Collectors.toList());
    }
    public static double crossProduct(Point a, Point b, Point c) {
        // Calculates the cross product of vectors (b-a) and (c-a)
        double x1 = b.x - a.x;
        double y1 = a.y - b.y;
        double x2 = c.x - a.x;
        double y2 = a.y - c.y;
        return x1 * y2 - x2 * y1;
    }
    private static Comparator<Point> pointComparator(Point startPoint) {
        return (a, b) -> {
            double angleA = Math.atan2(startPoint.y - a.y, a.x - startPoint.x);
            double angleB = Math.atan2(startPoint.y - b.y, b.x - startPoint.x);
            return Double.compare(angleA, angleB);
        };
    }
    public void reducePointsByDensity() {
        reducedPoints = points.stream()
                .filter(current -> shouldKeep(previous, current))
                .collect(Collectors.toList());
    }
    private boolean shouldKeep(Point[] previous, Point current) {
        boolean shouldKeep = previous[0] == null || distance(previous[0], current) > maxDistance;
        if (shouldKeep) {
            if (previous[0] != null) {
                //System.out.println("point : " + previous[0] + " / " + distance(previous[0], current));
            }
            previous[0] = current;
        }
        return shouldKeep;
    }
    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
    private double theta(Point p1, Point p2){
        return Math.atan2(p1.y - p2.y, p1.x - p2.x);
    }

    private int orientation(Point p, Point q, Point r) {
        double val = crossProduct(p, q, r);
        if (val == 0) {
            return 0;  // collinear
        } else if (val > 0) {
            return 1;  // clockwise
        } else {
            return 2;  // counterclockwise
        }
    }
    public void removeCollinearPoints() {
        Point P0 = start;
        removeCollinearPoints = sortedPoints.stream()
                .collect(Collectors.groupingBy(p -> theta(P0,p)))
                .values().stream()
                .map(collinearPoints ->
                        collinearPoints.stream()
                                .max(Comparator.
                                        comparingDouble(p -> distance(P0, p)))
                                .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Point> calculateConvexHull(List<Point> points){
        // Build the hull
        hull = new ArrayList<>();
        hull.add(points.get(0));
        hull.add(points.get(1));
        // Initialize size of modified list
        int m = 2;
        for (int i = 2; i < points.size(); i++) {
            System.out.println("i = " + i);
            System.out.println("hull : " + hull);
            System.out.println("points to check : " + hull.get(m-2) + " / " + hull.get(m-1) + " / " + points.get(i));
            System.out.println("orientation :" + orientation(hull.get(m-2), hull.get(m-1), points.get(i)));
            // Keep removing i while angle of i and i-1 is not counterclockwise
            while (m > 1 && orientation(hull.get(m-2), hull.get(m-1), points.get(i)) != 2) {
                System.out.println("point : " + hull.get(m-1) + " will be removed");
                hull.remove(m-1);
                m--;
                System.out.println("m--, m = " + m);
            }
            System.out.println("point : " + points.get(i) + " will be added");
            hull.add(points.get(i));
            m++;  // Update size of modified list
            System.out.println("m++, m = " + m);
        }
        // If hull size is less than 3, return an empty list
        if (m < 3) {
            return new ArrayList<>();
        }
        return hull;
    }

}

