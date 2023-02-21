package discovery1;

import junit.framework.TestCase;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

public class ConvexHullTest extends TestCase {

    public void testExtendTo10PointsHull() {
        ConvexHull test = new ConvexHull();
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 5));
        points.add(new Point(5, 0));
        List<Point> extendedPoints = test.extendTo10PointsHull(points);
        assertEquals(10, extendedPoints.size());
    }
}