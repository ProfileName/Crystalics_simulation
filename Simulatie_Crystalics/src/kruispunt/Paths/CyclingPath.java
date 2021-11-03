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
public class CyclingPath extends MultiUserPath {

    public CyclingPath(Point[] points) {
        super(points);
        this.color = Color.blue;
        sensorRange = 1;
    }
}
