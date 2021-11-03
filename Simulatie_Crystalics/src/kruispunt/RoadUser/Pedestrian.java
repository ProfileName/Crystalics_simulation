/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt.RoadUser;

import kruispunt.Paths.Path;

import java.awt.*;
import java.util.Random;

/**
 *
 * @author Len
 */
public final class Pedestrian extends RoadUser {

    private final float offset;

    public Pedestrian(Path path) {
        super(path);
        offset = new Random().nextInt(80) / 100.0f;
        interval = 1500;
    }

    @Override
    public void draw(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        Point p = getPosition();
        switch (direction) {
            case NORTH:
                g2.fillRect(p.x * CELLWIDTH + (int) (0.9 * CELLWIDTH), p.y * CELLHEIGHT + CELLHEIGHT / 5 + (int) (offset * CELLHEIGHT), CELLWIDTH / 6, CELLHEIGHT / 6);
                break;
            case WEST:
                g2.fillRect(p.x * CELLWIDTH + CELLWIDTH / 4, p.y * CELLHEIGHT + (int) (CELLHEIGHT), CELLWIDTH / 2, CELLHEIGHT / 6);
                break;
            case SOUTH:
                g2.fillRect(p.x * CELLWIDTH + (int) (0.9 * CELLWIDTH), p.y * CELLHEIGHT + CELLHEIGHT / 5 + (int) (offset * CELLHEIGHT), CELLWIDTH / 6, CELLHEIGHT / 6);
                break;
            case EAST:
                g2.fillRect(p.x * CELLWIDTH + (int) (offset * CELLWIDTH), p.y * CELLHEIGHT + (int) (CELLHEIGHT), CELLWIDTH / 6, CELLHEIGHT / 6);
                break;
            default:  //is never reached
                g2.fillRect(p.x * CELLWIDTH + (int) (0.75 * CELLWIDTH), p.y * CELLHEIGHT + CELLHEIGHT / 5, CELLWIDTH / 6, CELLHEIGHT / 6);
                break;
        }
    }

}
