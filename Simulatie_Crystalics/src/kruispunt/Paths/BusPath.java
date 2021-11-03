/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt.Paths;

import java.awt.*;

/**
 *
 * @author Len
 */
public final class BusPath extends Path {

    public BusPath(Point[] points, int sensorRange) {
        super(points);
        this.color = Color.GREEN;
        this.sensorRange = sensorRange;
    }
}
