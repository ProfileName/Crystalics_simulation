/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt.RoadUser;

import kruispunt.Paths.Path;
import kruispunt.TrafficLight;

import java.awt.*;

/**
 *
 * @author Len
 */
public abstract class RoadUser {

    public enum Direction {

        NORTH, EAST, SOUTH, WEST, NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST
    }

    public enum Status {

        ON_SCREEN, OFF_SCREEN
    }

    protected int interval;
    private long lastUpdateTime;

    protected Color color;
    protected final Path path;
    protected Direction direction;

    private Status status;
    protected int index;
    private TrafficLight currentLight;

    public RoadUser(Path path) {
        this.path = path;
        this.status = Status.ON_SCREEN;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void setLight(TrafficLight l) {
        currentLight = l;
    }

    public TrafficLight getLight() {
        return currentLight;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public int getIndex() {
        return index;
    }

    public Status getStatus() {
        return status;
    }

    public void update(long time) {
        if (time - lastUpdateTime > interval) {
            if (path.canMoveTo(this, index + 1)) {
                ++index;
                lastUpdateTime = time;
            }
        }
    }

    public void destroy() {
        status = Status.OFF_SCREEN;
    }

    public Point getPosition() {
        return path.getVectors()[index];
    }

    public abstract void draw(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT);

    protected Polygon getPolygon(Point p, int width, int height, int angle, int CELLWIDTH, int CELLHEIGHT) {
        Point center = new Point(p.x * CELLWIDTH + CELLWIDTH / 2, p.y * CELLHEIGHT + CELLHEIGHT / 2);

        int[][] rotatedPoints = getRotation(center.x, center.y,
                new int[]{p.x * CELLWIDTH + width / 2, p.x * CELLWIDTH + width, p.x * CELLWIDTH + width, p.x * CELLWIDTH + width / 2},
                new int[]{p.y * CELLHEIGHT + height / 3, p.y * CELLHEIGHT + height / 3, p.y * CELLHEIGHT + (int) (height * 1.33), p.y * CELLHEIGHT + (int) (height * 1.33)}, angle);

        return new Polygon(new int[]{rotatedPoints[0][0], rotatedPoints[1][0], rotatedPoints[2][0], rotatedPoints[3][0]},
                new int[]{rotatedPoints[0][1], rotatedPoints[1][1], rotatedPoints[2][1], rotatedPoints[3][1]}, 4);
    }

    protected int[][] getRotation(int cx, int cy, int[] xs, int[] ys, double angle) {
        double radians = (Math.PI / 180) * angle;

        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        int[][] rotatedPoints = new int[xs.length][];

        for (int i = 0; i < rotatedPoints.length; i++) {
            int x = xs[i];
            int y = ys[i];

            rotatedPoints[i] = new int[2];
            rotatedPoints[i][0] = (int) ((cos * (x - cx)) + (sin * (y - cy)) + cx);
            rotatedPoints[i][1] = (int) ((cos * (y - cy)) - (sin * (x - cx)) + cy);
        }
        return rotatedPoints;
    }
}
