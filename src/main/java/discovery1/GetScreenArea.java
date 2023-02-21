package discovery1;
import javax.swing.*;
import java.awt.*;

public class GetScreenArea {
    Point conner = null;
    javax.swing.Timer t;

    public void setConnerToNull() {
        this.conner = null;
    }

    public Point getConner() {
        return conner;
    }

    public void getPoint() {
        JFrame frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        AreaForAnalysis areaForAnalysis = new AreaForAnalysis();
        frame.addMouseListener(areaForAnalysis);
        frame.setUndecorated(true);
        frame.setOpacity(0.1f);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frame.dispose();
                t.stop();
            }
        });
        t = new javax.swing.Timer(1000, e -> {
            if (areaForAnalysis.getCoordinateX() != 0 && areaForAnalysis.getCoordinateY() != 0) { // strange condition
                conner = new Point(areaForAnalysis.getCoordinateX(), areaForAnalysis.getCoordinateY());
                frame.dispose();
                frame.removeMouseListener(areaForAnalysis);
                t.stop();
            }
        });
        t.start();
        System.out.println("Please select the point...");
    }
}