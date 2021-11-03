/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt;

import java.awt.*;

/**
 *
 * @author len
 */
public class BusLight extends TrafficLight {

    private final int line;
    public static final int STRAIGHT_RIGHT = 3;
    public static final int STRAIGHT = 4;
    public static final int RIGHT = 5;

    public BusLight(int id, int line, Point... points) {
        super(id, points);
        this.line = line;
    }

    public BusLight(int id, int line, Point[] points, Point[] offsets) {
        super(id, points, offsets);
        this.line = line;
    }

    @Override
    public void registerUser() {
        if (activeUsers == 0) {
            Main.getConnection().sendBusStatus(id, line, true);
            
        }
        activeUsers++;
    }

    @Override
    public void unregisterUser() {
        activeUsers--;
        if (activeUsers == 0) {
            Main.getConnection().sendBusStatus(id, line, false);
        }
    }

}
