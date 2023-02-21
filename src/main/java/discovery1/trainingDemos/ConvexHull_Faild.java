package discovery1.trainingDemos;

import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//  Graham Scan
public class ConvexHull_Faild {
    private Point start;
    private List<Point> sortedPoints;
    private List<Point> hull;
    private List<Point> points;

    public List<Point> getHull() {
        return hull;
    }

    public ConvexHull_Faild(List<Point> points) {
        this.points = points;
    }

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

    private void removeDuplicatePoints (){
        Set<Point> uniquePoints = new HashSet<>(points);
        points.clear();
        points.addAll(uniquePoints);
    }

    private void findP0 (){
        // Find the point with the lowest y-coordinate (or the leftmost one in case of tie)
        start = points.get(0);
        for (Point point : points) {
            if (point.y < start.y || (point.y == start.y && point.x < start.x)) {
                start = point;
            }
        }
    }

    private void sortThePoints () {
        // Sort the points by polar angle with respect to the start point
        final Point startPoint = start;
        sortedPoints = new ArrayList<>(points);
        sortedPoints.sort((a, b) -> {
            double angleA = Math.atan2(a.y - startPoint.y, a.x - startPoint.x);
            double angleB = Math.atan2(b.y - startPoint.y, b.x - startPoint.x);
            return Double.compare(angleA, angleB);
        });
    }

    private static double crossProduct(Point a, Point b, Point c) {
        // Calculates the cross product of vectors (b-a) and (c-a)
        double x1 = b.x - a.x;
        double y1 = b.y - a.y;
        double x2 = c.x - a.x;
        double y2 = c.y - a.y;
        return x1 * y2 - x2 * y1;
    }
}

