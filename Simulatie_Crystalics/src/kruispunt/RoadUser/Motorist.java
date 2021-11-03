/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt.RoadUser;

import kruispunt.Paths.Path;

import java.awt.*;

/**
 *
 * @author Len
 */
public final class Motorist extends RoadUser {


    public Motorist(Path path) {
        super(path);
        setColor(Color.DARK_GRAY);
        interval = 400;
    }

    @Override
    public void draw(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        int width = (int) ((float) CELLWIDTH / 1.5f);
        int height = (int) ((float) CELLHEIGHT / 1.5f);
        Point p = getPosition();
        g2.setColor(color);
        switch (direction) {
            case NORTH:
                g2.fillRect(p.x * CELLWIDTH + width / 2, p.y * CELLHEIGHT + height / 3, width / 2, height);
                break;
            case WEST:
                g2.fillRect(p.x * CELLWIDTH + width / 3, p.y * CELLHEIGHT + height / 2, width, height / 2);
                break;
            case SOUTH:
                g2.fillRect(p.x * CELLWIDTH + width / 2, p.y * CELLHEIGHT + height / 3, width / 2, height);
                break;
            case EAST:
                g2.fillRect(p.x * CELLWIDTH + width / 3, p.y * CELLHEIGHT + height / 2, width, height / 2);
                break;
            case NORTH_EAST:
            case SOUTH_WEST:
                g2.fillPolygon(getPolygon(p, width, height, -34, CELLWIDTH, CELLHEIGHT));
                break;
            case SOUTH_EAST:
            case NORTH_WEST:
                g2.fillPolygon(getPolygon(p, width, height, 34, CELLWIDTH, CELLHEIGHT));
                break;
        }

    }
}
