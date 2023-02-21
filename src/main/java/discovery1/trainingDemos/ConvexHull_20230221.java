package discovery1.trainingDemos;

import org.opencv.core.Point;

import java.util.*;
import java.util.stream.Collectors;

//  Graham Scan
public class ConvexHull_20230221 {
    private Point start;
    private List<Point> sortedPoints;
    private List<Point> reducedPoints;
    private Point[] previous = {null};
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
    public ConvexHull_20230221(List<Point> points) {
        this.points = points;
    }
    public ConvexHull_20230221() {}

 // WORKED. extendTo10PointsHull method extremely crude
    public void getConvexHull() {

        removeDuplicatePoints ();
        findP0 ();
        reducePointsByDensity();
        removeCollinearPoints();
        sortThePointsByAngle();
        findP0 (sortedPoints);

        calculateConvexHull(sortedPoints);

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
        sortedPoints = removeCollinearPoints.stream()
                .sorted(pointComparator(startPoint))
                .collect(Collectors.toList());
    }
    public static double crossProduct(Point p1, Point p2, Point p3) {
        // Calculates the cross product of vectors
        return (p1.y - p2.y) * (p3.x - p2.x) - (p2.x - p1.x) * (p2.y - p3.y);

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

    public List<Point> reduceTo10PointsHull(List<Point> pointsToReduce) {
        List<Point> result = new ArrayList<>(pointsToReduce);
        while (result.size() > 10) {
            double minDistance = Double.MAX_VALUE;
            Point minP1 = null;
            Point minP2 = null;
            for (Point p1 : result) {
                for (Point p2 : result) {
                    if (p1 == p2) {
                        continue;
                    }
                    double dist = distance(p1, p2);
                    if (dist < minDistance) {
                        minDistance = dist;
                        minP1 = p1;
                        minP2 = p2;
                    }
                }
            }
            if (minP2 != result.get(0)){
                result.remove(minP2);
            }
            else if (minP1 != result.get(0)) {
                result.remove(minP1);
            }
        }
        return result;
    }

    public List<Point> extendTo10PointsHull(List<Point> pointsToExtend) {
        List<Point> result = new ArrayList<>(pointsToExtend);
        Set<Point> excludedPoints = new HashSet<>();
        while (result.size() < 10) {
            double maxDistance = Double.MIN_VALUE;
            Point maxP1 = null;
            Point maxP2 = null;
            for (Point p1 : result) {
                for (Point p2 : result) {
                    if (p1 == p2 || excludedPoints.contains(p2)) {
                        continue;
                    }
                    double dist = distance(p1, p2);
                    if (dist > maxDistance) {
                        maxDistance = dist;
                        maxP1 = p1;
                        maxP2 = p2;
                    }
                }
            }
            if (maxP1 != null) {
                int index1 = result.indexOf(maxP1);
                int index2 = result.indexOf(maxP2);
                int insertIndex = Math.max(index1, index2);
                Point calculatedMiddlePoint = middlePoint(maxP1, maxP2);
                excludedPoints.add(maxP2);
                excludedPoints.add(maxP1);
                result.add(insertIndex, calculatedMiddlePoint);
                System.out.println(maxP1);
                System.out.println(maxP2);
                System.out.println("new Point added: " + calculatedMiddlePoint);

            }
        }
        return result;
    }

    public List<Point> normalizeHullTo10Points (List<Point> hull){
        if (hull.size() > 10){
            return reduceTo10PointsHull(hull);
        }
        else {
            return extendTo10PointsHull(hull);
        }
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
        removeCollinearPoints = reducedPoints.stream()
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
            /*
            System.out.println("i = " + i);
            System.out.println("hull : " + hull);
            System.out.println("points to check : " + hull.get(m-2) + " / " + hull.get(m-1) + " / " + points.get(i));
            System.out.println("orientation :" + orientation(hull.get(m-2), hull.get(m-1), points.get(i)));

             */
            // Keep removing i while angle of i and i-1 is not counterclockwise
            while (m > 1 && orientation(hull.get(m-2), hull.get(m-1), points.get(i)) != 2) {
                //System.out.println("point : " + hull.get(m-1) + " will be removed");
                hull.remove(m-1);
                m--;
                //System.out.println("m--, m = " + m);
            }
            //System.out.println("point : " + points.get(i) + " will be added");
            hull.add(points.get(i));
            m++;  // Update size of modified list
            //System.out.println("m++, m = " + m);
        }
        // If hull size is less than 3, return an empty list
        if (m < 3) {
            return new ArrayList<>();
        }
        return hull;
    }

    private static Point middlePoint (Point maxP1, Point maxP2){
        return new Point((maxP1.x + maxP2.x) / 2, (maxP1.y + maxP2.y) / 2);
    }

}

