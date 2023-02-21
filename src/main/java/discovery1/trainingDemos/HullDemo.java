package discovery1.trainingDemos;

import discovery1.ConvexHull;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class HullDemo {

    public static void main(String[] args) {

        Scalar whiteColor = new Scalar(255, 0, 0);

        List<Point> inputPoints = new ArrayList<>();
        inputPoints.add(new Point(0, 3));
        inputPoints.add(new Point(1, 1));
        inputPoints.add(new Point(2, 2));
        inputPoints.add(new Point(4, 4));
        inputPoints.add(new Point(0, 0));
        inputPoints.add(new Point(1, 2));
        inputPoints.add(new Point(3, 1));
        inputPoints.add(new Point(3, 3));

        ConvexHull calculateHull = new ConvexHull(inputPoints);
        calculateHull.getConvexHull();
        System.out.println(calculateHull.getHull());


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String initialScreenShotPath ="D:/screenshot-hull.png";
        String imageFromPointsPath = "D:/hull.png";
        Scalar color = new Scalar(255, 0, 0);
        Mat image = Imgcodecs.imread(initialScreenShotPath);

        Mat imageFromPoints = new Mat(image.size(), image.type());
        imageFromPoints.setTo(new Scalar(0, 0, 0));
        for (Point point : inputPoints) {
            Imgproc.circle(imageFromPoints, point, 1, color, -1);
        }
        Imgcodecs.imwrite(imageFromPointsPath, imageFromPoints);


    }
}







