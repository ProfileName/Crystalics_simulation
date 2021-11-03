/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt.Paths;

import kruispunt.Main;
import kruispunt.RoadUser.RoadUser;
import kruispunt.RoadUser.RoadUser.Direction;
import kruispunt.TrafficLight;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Len
 */
public class Path {

    //collection class
    protected class LightPoint {

        public final TrafficLight light;
        public int[] stopIndices;
        public int leaveIndex;

        public LightPoint(TrafficLight light, int leaveIndex, int[] stopIndices) {
            this.light = light;
            this.leaveIndex = leaveIndex;
            this.stopIndices = stopIndices;
        }
    }

    protected final Point[] points;
    protected Color color = Color.WHITE;
    protected final ArrayList<LightPoint> lightPoints = new ArrayList<>();
    protected final ArrayList<RoadUser> users;
    protected int sensorRange = 4;
    private boolean visible;
    private final Direction spawnDirection;

    public Path(Point[] points) {
        users = new ArrayList<>();
        this.points = points;
        spawnDirection = getDirection(0, 1);
    }

    private LightPoint getLightPoint(TrafficLight light) {
        for (LightPoint p : lightPoints) {
            if (p.light == light) {
                return p;
            }
        }
        return null;
    }

    protected final Direction getDirection(int index, int nextIndex) {

        Point a = points[index];
        Point b = points[nextIndex];

        if (b.y < a.y) {
            if (b.x > a.x) {
                return Direction.NORTH_EAST;
            }
            if (b.x < a.x) {
                return Direction.NORTH_WEST;
            }
            return Direction.NORTH;
        }
        if (b.y > a.y) {
            if (b.x > a.x) {
                return Direction.SOUTH_EAST;
            }
            if (b.x < a.x) {
                return Direction.SOUTH_WEST;
            }
            return Direction.SOUTH;
        }

        if (b.x > a.x) {
            return Direction.EAST;
        }
        return Direction.WEST;
    }

    public void addUser(RoadUser v) {
        users.add(v);
        v.setDirection(spawnDirection);
    }

    public void addTrafficLight(TrafficLight light, int leaveIndex, int... stopIndices) {
        LightPoint p = new LightPoint(light, leaveIndex, stopIndices);
        lightPoints.add(p);
    }

    public boolean isEmpty(int index) {
        for (RoadUser v : Main.getWorld().getRoadUsers()) { //check for global occupation
            if (v.getPosition().x == points[index].x && v.getPosition().y == points[index].y) {
                return false;
            }
        }
        return true;
    }

    protected void checkLeaveSensor(RoadUser vehicle, int index) {
        if (vehicle.getLight() != null) {
            TrafficLight l = vehicle.getLight();
            if (getLightPoint(l).leaveIndex <= index) {
                l.unregisterUser();
                vehicle.setLight(null);
            }
        }
    }

    public boolean canMoveTo(RoadUser vehicle, int index) {

        if (index == points.length) {
            vehicle.destroy();
            users.remove(vehicle);
            return false;
        }
        if (!isEmpty(index)) {
            return false;
        }

        for (LightPoint p : lightPoints) {
            for (int stopIndex : p.stopIndices) {
                if (vehicle.getLight() == null) { //arrive sensor check
                    int dist = stopIndex - index;
                    if (dist > -1 && dist <= sensorRange) {
                        vehicle.setLight(p.light);
                        p.light.registerUser();
                    }
                }
                if (stopIndex == index && p.light.getStatus() == TrafficLight.Status.RED) {
                    return false;
                }
            }
        }
        checkLeaveSensor(vehicle, index);

        vehicle.setDirection(getDirection(index - 1, index));
        return true;
    }

    public Point[] getVectors() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public void setVisibile(boolean v) {
        this.visible = v;
    }

    public void draw(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        if (visible) {
            g2.setColor(color);
            int index = 0;
            for (Point p : points) {
                
                g2.drawString(Integer.toString(index), p.x * CELLWIDTH +CELLWIDTH/3, p.y * CELLHEIGHT + CELLHEIGHT/3);
                g2.fillOval(p.x * CELLWIDTH + CELLWIDTH / 3, p.y * CELLHEIGHT + CELLHEIGHT / 3, CELLWIDTH / 4, CELLHEIGHT / 4);
            ++index;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();
        for (Point point : points) {
            sB.append(point);
            sB.append(" ");
        }
        return sB.toString();
    }
}
