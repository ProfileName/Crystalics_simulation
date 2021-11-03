/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt;

import java.awt.*;

/**
 *
 * @author Len
 */
public class TrafficLight {

    public enum Status {

        RED,
        GREEN,
        ORANGE
    }

    private static boolean idVisible;
    protected final int id;
    protected int activeUsers;

    private Status status;
    private Point[] pointOffsets;
    private Point[] points;

    public int getId() {
        return id;
    }

    public void unregisterUser() {
        activeUsers--;
        if (activeUsers == 0) {
            Main.getConnection().sendStatus(id, false);
        }
    }

    public void registerUser() {
        if (activeUsers == 0) {
            Main.getConnection().sendStatus(id, true);
        }
        activeUsers++;
    }

    public TrafficLight(int id, Point... points) {
        this.id = id;
        this.points = points;
        status = Status.RED;
    }

    public TrafficLight(int id, Point[] points, Point[] offsets) {
        this(id, points);
        this.pointOffsets = offsets;
    }

    public static void setIdVisibility(boolean v) {
        idVisible = v;
    }

    private Color getColor() {
        switch (status) {
            case GREEN:
                return Color.GREEN;
            case ORANGE:
                return Color.ORANGE;
            case RED:
            default:
                return Color.RED;
        }
    }

    public void draw(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        g2.setColor(getColor());
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Point o = pointOffsets == null ? new Point(0, 0) : pointOffsets[i];
            int offsetWidth = (int) ((((float) o.x) / 100) * ((float) CELLWIDTH));
            int offsetHeight = (int) ((((float) o.y) / 100) * ((float) CELLHEIGHT));
            g2.fillOval(p.x * CELLWIDTH + offsetWidth, p.y * CELLHEIGHT + offsetHeight, CELLWIDTH / 4, CELLHEIGHT / 4);
            if (idVisible) {
                g2.setColor(Color.black);
                g2.drawString(Integer.toString(id), p.x * CELLWIDTH + offsetWidth, p.y * CELLHEIGHT + offsetHeight);
                g2.setColor(getColor());
            }
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
