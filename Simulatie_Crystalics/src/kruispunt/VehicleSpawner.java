/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt;

import kruispunt.Paths.BusPath;
import kruispunt.Paths.CyclingPath;
import kruispunt.Paths.Path;
import kruispunt.Paths.PedPath;
import kruispunt.RoadUser.BusDriver;
import kruispunt.RoadUser.Cyclist;
import kruispunt.RoadUser.Motorist;
import kruispunt.RoadUser.Pedestrian;

import java.util.Random;

/**
 *
 * @author Len
 */
public final class VehicleSpawner {

    private boolean running;
    private final Random random = new Random();
    private final World world;
    private long lastTime;
    private int interval = 1000;
    private final Path[] paths;

    public VehicleSpawner(World w) {
        this.world = w;
        paths = world.getPaths();
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void setInterval(int millis) {
        this.interval = millis;
    }

    public int getInterval() {
        return interval;
    }

    public void update() {
        if (running && System.currentTimeMillis() - lastTime > interval) {
            int index = random.nextInt(paths.length);
            spawnOnPath(index);
            lastTime = System.currentTimeMillis();
        }
    }

    public void spawnOnPath(int index) {
        Path path = paths[index];
        if (path.isEmpty(0)) {
            if (path instanceof CyclingPath) {
                world.addRoadUser(new Cyclist(path), path);
            } else if (path instanceof BusPath) {
                world.addRoadUser(new BusDriver(path), path);
            } else if (path instanceof PedPath) {
                world.addRoadUser(new Pedestrian(path), path);
            } else {
                world.addRoadUser(new Motorist(path), path);
            }
        }
    }
}
