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
public class PedPath extends MultiUserPath {

    public PedPath(Point[] points) {
        super(points);
        color = Color.PINK;
        sensorRange = 1;
    }
}