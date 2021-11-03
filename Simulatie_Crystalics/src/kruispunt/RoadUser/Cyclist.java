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
public final class Cyclist extends RoadUser {

    public Cyclist(Path path) {
        super(path);
        setColor(Color.LIGHT_GRAY);
        interval = 800 + new Random().nextInt(400);
    }

    @Override
    public void draw(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        Point p = getPosition();
        switch (direction) {
            case NORTH:
                g2.fillRect(p.x * CELLWIDTH + (int) (0.75 * CELLWIDTH), p.y * CELLHEIGHT + CELLHEIGHT / 5, CELLWIDTH / 6, CELLHEIGHT / 2);
                break;
            case WEST:
                g2.fillRect(p.x * CELLWIDTH + CELLWIDTH / 4, p.y * CELLHEIGHT + (int) (0.10 * CELLHEIGHT), CELLWIDTH / 2, CELLHEIGHT / 6);
                break;
            case SOUTH:
                g2.fillRect(p.x * CELLWIDTH + (int) (0.10 * CELLWIDTH), p.y * CELLHEIGHT + CELLHEIGHT / 5, CELLWIDTH / 6, CELLHEIGHT / 2);
                break;
            case EAST:
                g2.fillRect(p.x * CELLWIDTH + CELLWIDTH / 4, p.y * CELLHEIGHT + (int) (0.75 * CELLHEIGHT), CELLWIDTH / 2, CELLHEIGHT / 6);
                break;
            default:  //is never reached
                g2.fillRect(p.x * CELLWIDTH + (int) (0.75 * CELLWIDTH), p.y * CELLHEIGHT + CELLHEIGHT / 5, CELLWIDTH / 6, CELLHEIGHT / 2);
                break;
        }
    }
}
