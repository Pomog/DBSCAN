package discovery1;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AreaForAnalysis implements MouseListener {
    private int coordinateX;
    public int getCoordinateX() {
        return coordinateX;
    }
    public int getCoordinateY() {
        return coordinateY;
    }
    int coordinateY;

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked");
        if (SwingUtilities.isLeftMouseButton(e)){
        coordinateX = (int)(e.getLocationOnScreen().getX());
        coordinateY = (int)(e.getLocationOnScreen().getY());
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
