package discovery1.trainingDemos;

import org.opencv.core.Point;

import java.util.*;
import java.util.stream.Collectors;

//  Graham Scan
public class ConvexHull_20230129 {
    private Point start;
    private List<Point> sortedPoints;

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
    public ConvexHull_20230129(List<Point> points) {
        this.points = points;
    }

 // NOT WORKED
    public void getConvexHull() {

        removeDuplicatePoints ();
        findP0 ();
        sortThePoints ();

        // Use Graham's scan algorithm to find the convex hull
       hull = new ArrayList<>();
        hull.add(sortedPoints.get(0));
        hull.add(sortedPoints.get(1));
        Point nextToTop = sortedPoints.get(0);
        for (int i = 2; i < sortedPoints.size(); i++) {
            Point top = hull.get(hull.size() - 1);
            Point current = sortedPoints.get(i);

            // Check if the current point makes a left turn
            while (hull.size() > 1 && crossProduct(nextToTop, top, current) <= 0) {
                hull.remove(hull.size() - 1);
                top = nextToTop;
                if (hull.size() > 2) {
                    nextToTop = hull.get(hull.size() - 2);
                }
            }
            hull.add(current);
            }

    }

    // worked
    public void removeDuplicatePoints (){
        Set<Point> uniquePoints = new HashSet<>(points);
        points.clear();
        points.addAll(uniquePoints);
    }

    // worked
    public void findP0 (){
        // Find the lowest point, !!!y=0 top of the screen!!! (or the leftmost one in case of tie)
        start = points.stream()
                .reduce((p1, p2) -> p1.y > p2.y || (p1.y == p2.y && p1.x < p2.x) ? p1 : p2)
                .orElse(points.get(0));
    }

    // worked
    public void sortThePoints () {
        // Sort the points by polar angle with respect to the start point
        final Point startPoint = start;
        sortedPoints = points.stream()
                .sorted(pointComparator(startPoint))
                .collect(Collectors.toList());
    }

    public static double crossProduct(Point a, Point b, Point c) {
        // Calculates the cross product of vectors (b-a) and (c-a)
        double x1 = b.x - a.x;
        double y1 = b.y - a.y;
        double x2 = c.x - a.x;
        double y2 = c.y - a.y;
        return x1 * y2 - x2 * y1;
    }

    private static Comparator<Point> pointComparator(Point startPoint) {
        return (a, b) -> {
            double angleA = Math.atan2(startPoint.y - a.y, a.x - startPoint.x);
            double angleB = Math.atan2(startPoint.y - b.y, b.x - startPoint.x);
            return Double.compare(angleA, angleB);
        };
    }
}

