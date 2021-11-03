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
abstract class MultiUserPath extends Path {

    public MultiUserPath(Point[] points) {
        super(points);
    }

    @Override
    public boolean isEmpty(int index) {
        return true;
    }
}
