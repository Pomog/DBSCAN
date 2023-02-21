package discovery1.trainingDemos;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Image_01 {
    private Mat image;
    private List<Point> points;
    private String initialScreenShotPath = "D:/screenshot.png";
    private String processedScreenShotPath = "D:/screenshot_bright.png";
    private String convertedToBinaryScreenShotPath = "D:/screenshot_bright_binary.png";
    private String imageFromPointsPath = "D:/screenshot_points.png";
    int radius = 3;
    Scalar color = new Scalar(255, 0, 0);

    public List<Point> getPoints() {
        return points;
    }

    public void processingScreenShotToBinary(){
        // Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Read image file
        image = Imgcodecs.imread(initialScreenShotPath, Imgcodecs.IMREAD_GRAYSCALE);
        Mat brightenedImage = new Mat(image.size(), image.type());
        Core.addWeighted(image, 1.5, brightenedImage, 0.0, 0, brightenedImage);
        Imgcodecs.imwrite(processedScreenShotPath, brightenedImage);
        // Check if image is loaded properly
        if(image.empty()) {
            System.out.println("Error: Image not loaded properly.");
            return;
        }
        // Apply threshold to the image to convert dots to white pixels
        Imgproc.threshold(brightenedImage, brightenedImage, 50, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite(convertedToBinaryScreenShotPath, brightenedImage);
    }
    //Overload
    public void processingScreenShotToBinary(String initialScreenShotPath, String convertedToBinaryScreenShotPath){
        // Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Read image file
        image = Imgcodecs.imread(initialScreenShotPath, Imgcodecs.IMREAD_GRAYSCALE);
        Mat brightenedImage = new Mat(image.size(), image.type());
        Core.addWeighted(image, 1.5, brightenedImage, 0.0, 0, brightenedImage);
        Imgcodecs.imwrite(processedScreenShotPath, brightenedImage);
        // Check if image is loaded
        if(image.empty()) {
            System.out.println("Error: Image not loaded properly.");
            return;
        }
        // Apply threshold to the image to convert dots to white pixels
        Imgproc.threshold(brightenedImage, brightenedImage, 50, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite(convertedToBinaryScreenShotPath, brightenedImage);
    }

    public List<Point> pixelCoordinatesToList (@NotNull Mat image){
        List<Point> points = new ArrayList<>();
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                double[] color = image.get(y, x);
                if (color[0] == 255.0){
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    public void createImageFromPointList (@NotNull List<Point> points, Scalar color){
        Mat imageFromPoints = new Mat(image.size(), image.type());
        imageFromPoints.setTo(new Scalar(0, 0, 0));
        for (Point point : points) {
            Imgproc.circle(imageFromPoints, point, 1, color, -1);
        }
        Imgcodecs.imwrite(imageFromPointsPath, imageFromPoints);
    }
    //Overload
    public void createImageFromPointList (@NotNull List<Point> points, Scalar color, String imageFromPointsPath){
        Mat imageFromPoints = new Mat(image.size(), image.type());
        imageFromPoints.setTo(new Scalar(0, 0, 0));
        for (Point point : points) {
            Imgproc.circle(imageFromPoints, point, 1, color, -1);
        }
        Imgcodecs.imwrite(imageFromPointsPath, imageFromPoints);
    }

    public double[][] pointsTo2DArray (List<Point> points){
            /*
                double[][] pointsArray = new double[points.size()][2];
                for (int i = 0; i < points.size(); i++) {
                pointsArray[i][0] = points.get(i).x;
                pointsArray[i][1] = points.get(i).y;
        }
        return pointsArray;
    */
//26.01.2023 The first time I used Stream API
                return points.stream()
                .map(p -> new double[] {p.x, p.y})
                .toArray(double[][]::new);
    }
}
