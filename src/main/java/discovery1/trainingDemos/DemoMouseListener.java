package discovery1.trainingDemos;
import discovery1.AreaForAnalysis;

import javax.swing.*;

public class DemoMouseListener {

    static javax.swing.Timer t;

    public static void main(String[] args) {
        int x;
        int y;

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
                System.exit(0);
            }
        });

        System.out.println("Please select the corner...");

       t = new javax.swing.Timer(1000, e -> {
                        if (areaForAnalysis.getCoordinateX() != 0 && areaForAnalysis.getCoordinateY() != 0) {
                System.out.println(areaForAnalysis.getCoordinateX() + "  " + areaForAnalysis.getCoordinateY());

                            frame.dispose();
                            frame.removeMouseListener(areaForAnalysis);
                            t.stop();
            }
        });
        t.start();

    }

}
