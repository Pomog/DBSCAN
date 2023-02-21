package discovery1;

import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

public class MouseDriver {
   private java.awt.Point leftTop;

    public MouseDriver(java.awt.Point leftBottom) {
        this.leftTop = leftBottom;
    }

    public void drawHull(List<Point> hullPoints) {
        int i = 0;
        int shiftX = (int) leftTop.x;
        int shiftY = (int) leftTop.y;
        Point startingPoint = hullPoints.get(0);
        List<Point> hullPointsClosed = new ArrayList<>(hullPoints);
        hullPointsClosed.add(startingPoint);

        try {
            Robot robot = new Robot();
            for (Point point : hullPointsClosed) {
                i++;
                int x = (int) point.x + shiftX;
                int y = (int) point.y + shiftY;
                robot.mouseMove(x, y);
                Thread.sleep(50);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                System.out.println("click " + i + " : " + x + " " + y);
                Thread.sleep(50);
            }
        } catch (AWTException e) {
            System.err.println("An error occurred while creating the robot: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("The thread was interrupted: " + e.getMessage());
        }
    }

    public void mouseClick (java.awt.Point submitButton){
        try {
            Robot robot = new Robot();
                int x = (int) submitButton.x;
                int y = (int) submitButton.y;
                robot.mouseMove(x, y);
                Thread.sleep(50);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                System.out.println("click submitButton: " + x + " " + y);
                Thread.sleep(50);
          } catch (AWTException e) {
            System.err.println("An error occurred while creating the robot: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("The thread was interrupted: " + e.getMessage());
        }
    }

}
