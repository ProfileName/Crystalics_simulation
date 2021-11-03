/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Len
 */
public class MyLabel extends JLabel {

    private final World world;

    public MyLabel(World world) 
    {
        this.world = world;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        world.draw(g, getWidth(), getHeight());
    }
}
