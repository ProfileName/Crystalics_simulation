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
import kruispunt.RoadUser.RoadUser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Len
 */
public class World {

    private char[][] grid;
    private Path[] paths;
    private final HashMap<Integer, TrafficLight> lights;
    private final ArrayList<RoadUser> roadUsers;

  

    private VehicleSpawner spawner;
    private int columns, rows;

    public int getPathCount() {
        return paths.length;
    }

    public int getLightCount() {
        return lights.size();
    }
    
   


    /*returns buslights corresponding to 1 id, since light 16 is really two lights in the code*/
    private ArrayList<BusLight> getBusLights(int id, int status) {
        ArrayList<BusLight> bLights = new ArrayList<>();
        switch (id) {
            case 15:
                bLights.add((BusLight) lights.get(15));
                break;
            case 16:
                switch (status) {
                    case 3:
                        bLights.add((BusLight) lights.get(16));
                        bLights.add((BusLight) lights.get(166));
                        break;
                    case 4:
                        bLights.add((BusLight) lights.get(16));
                        break;
                    case 5:
                        bLights.add((BusLight) lights.get(166));
                        break;
                    default:
                        bLights.add((BusLight) lights.get(16));
                        bLights.add((BusLight) lights.get(166));
                        break;
                }
                break;
        }
        return bLights;
    }

    public ArrayList<RoadUser> getRoadUsers() {
        return roadUsers;
    }

    public World() {
        lights = new HashMap<>();
        roadUsers = new ArrayList<>();
    }

    public Path[] getPaths() {
        return paths;
    }

    public VehicleSpawner getTrafficSpawner() {
        return spawner;
    }

    private Point[] getPoints(int... ds) {
        if (ds.length % 2 != 0) {
            return new Point[]{};
        }
        Point[] points = new Point[ds.length / 2];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(ds[i * 2], ds[i * 2 + 1]);
        }
        return points;
    }

    public void load() throws FileNotFoundException, IOException {
        ArrayList<char[]> rrows = new ArrayList<>();
        final String PATH_TO_MAP = "Map/map.txt";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH_TO_MAP)))) {
            char[] row;
            String line;
            while ((line = br.readLine()) != null) {
                columns = line.length();
                row = new char[columns];

                for (int i = 0; i < row.length; i++) {
                    row[i] = line.charAt(i);
                }
                rrows.add(row);
            }
            rows = rrows.size();
            grid = new char[rows][];
            for (int i = 0; i < grid.length; i++) {
                grid[i] = rrows.get(i);
            }
        }

        addLight(new TrafficLight(1, new Point[]{new Point(20, 7)}, new Point[]{new Point(30, 30)}));
        addLight(new TrafficLight(2, new Point[]{new Point(19, 7)}, new Point[]{new Point(30, 30)}));
        addLight(new TrafficLight(3, new Point[]{new Point(18, 7)}, new Point[]{new Point(30, 30)}));
        addLight(new TrafficLight(4, new Point[]{new Point(17, 7)}, new Point[]{new Point(30, 30)}));

        addLight(new TrafficLight(0, new Point[]{new Point(11, 7)}, new Point[]{new Point(50, 50)}));

        addLight(new TrafficLight(5, new Point[]{new Point(15, 10), new Point(15, 9)}, new Point[]{new Point(30, 30), new Point(30, 50)}));
        addLight(new TrafficLight(6, new Point[]{new Point(15, 11)}, new Point[]{new Point(30, 30)}));
        addLight(new TrafficLight(7, new Point[]{new Point(15, 12), new Point(15, 13)}, new Point[]{new Point(30, 30), new Point(30, 10)}));

        addLight(new TrafficLight(8, new Point[]{new Point(21, 13), new Point(20, 13)}, new Point[]{new Point(30, 50), new Point(50, 50)}));
        addLight(new TrafficLight(9, new Point[]{new Point(22, 13)}, new Point[]{new Point(40, 50)}));
        addLight(new TrafficLight(10, new Point[]{new Point(23, 13)}, new Point[]{new Point(40, 50)}));
        addLight(new TrafficLight(11, new Point[]{new Point(24, 13), new Point(26, 13)}, new Point[]{new Point(40, 50), new Point(30, 50)}));

        addLight(new TrafficLight(12, new Point[]{new Point(24, 10)}, new Point[]{new Point(30, 40)}));
        addLight(new TrafficLight(13, new Point[]{new Point(24, 6), new Point(24, 10)}, new Point[]{new Point(30, 60), new Point(30, 10)}));
        addLight(new TrafficLight(14, new Point[]{new Point(24, 6)}, new Point[]{new Point(30, 30)}));

        //buslights
        addLight(new BusLight(15, 70, new Point[]{new Point(15, 7)}, new Point[]{new Point(60, 30)}));
        addLight(new BusLight(16, 97, new Point[]{new Point(26, 18)}, new Point[]{new Point(20, 70)})); //rechtdoor
        lights.put(166, new BusLight(16, 71, new Point[]{new Point(26, 18)}, new Point[]{new Point(45, 70)}));  //rechtsaf

        addLight(new TrafficLight(17, new Point[]{new Point(24, 5)}, new Point[]{new Point(30, 0)}));
        addLight(new TrafficLight(19, new Point[]{new Point(14, 7)}, new Point[]{new Point(4, 70)}));
        addLight(new TrafficLight(20, new Point[]{new Point(15, 13)}, new Point[]{new Point(0, 0)}));
        addLight(new TrafficLight(21, new Point[]{new Point(26, 14)}, new Point[]{new Point(20, 70)}));
        addLight(new TrafficLight(22, new Point[]{new Point(16, 16)}, new Point[]{new Point(70, 0)}));
        addLight(new TrafficLight(23, new Point[]{new Point(21, 5)}, new Point[]{new Point(50, 0)}));
        addLight(new TrafficLight(24, new Point[]{new Point(24, 5)}, new Point[]{new Point(0, 0)}));
        addLight(new TrafficLight(25, new Point[]{new Point(21, 5)}));
        addLight(new TrafficLight(26, new Point[]{new Point(15, 5)}, new Point[]{new Point(60, 1)}));
        addLight(new TrafficLight(27, new Point[]{new Point(13, 7)}, new Point[]{new Point(50, 60)}));
        addLight(new TrafficLight(28, new Point[]{new Point(13, 9)}, new Point[]{new Point(50, 30)}));
        addLight(new TrafficLight(29, new Point[]{new Point(13, 9)}, new Point[]{new Point(50, 60)}));
        addLight(new TrafficLight(30, new Point[]{new Point(13, 13)}, new Point[]{new Point(50, 0)}));
        addLight(new TrafficLight(31, new Point[]{new Point(20, 16)}, new Point[]{new Point(30, 20)}));
        addLight(new TrafficLight(32, new Point[]{new Point(16, 16)}, new Point[]{new Point(70, 20)}));
        addLight(new TrafficLight(33, new Point[]{new Point(26, 16)}, new Point[]{new Point(20, 20)}));
        addLight(new TrafficLight(34, new Point[]{new Point(20, 16)}, new Point[]{new Point(60, 20)}));

        //west
        Path path1 = new Path(getPoints(17, 0, 17, 1, 17, 2, 17, 3, 17, 4, 17, 5, 17, 6, 17, 7, 17, 8, 16, 8, 15, 8, 14, 8, 13, 8, 12, 8, 11, 8, 10, 8, 9, 8, 8, 8, 7, 8, 6, 8, 5, 8, 4, 8, 3, 8, 2, 8, 1, 8, 0, 8));
        path1.addTrafficLight(lights.get(4), 8, 5, 7);

        Path path2 = new Path(getPoints(18, 0, 18, 1, 18, 2, 18, 3, 18, 4, 18, 5, 18, 6, 18, 7, 18, 8, 18, 9, 18, 10, 18, 11, 18, 12, 18, 13, 18, 14, 18, 15, 18, 16, 18, 17, 18, 18, 18, 19, 18, 20, 18, 21));
        path2.addTrafficLight(lights.get(3), 8, 5, 7);

        Path path3 = new Path(getPoints(19, 0, 19, 1, 19, 2, 19, 3, 19, 4, 19, 5, 19, 6, 19, 7, 19, 8, 19, 9, 19, 10, 19, 11, 19, 12, 19, 13, 19, 14, 19, 15, 19, 16, 19, 17, 19, 18, 19, 19, 19, 20, 19, 21));
        path3.addTrafficLight(lights.get(2), 8, 5, 7);

        Path path4 = new Path(getPoints(20, 0, 20, 1, 20, 2, 20, 3, 20, 4, 20, 5, 20, 6, 20, 7, 20, 8, 20, 9, 21, 10, 22, 11, 23, 11, 24, 11, 25, 11, 26, 11, 27, 11, 28, 11, 29, 11, 30, 11, 31, 11, 32, 11));
        path4.addTrafficLight(lights.get(1), 8, 5, 7);
        //

        //south
        Path path5 = new Path(getPoints(0, 10, 1, 10, 2, 10, 3, 10, 4, 10, 5, 10, 6, 10, 7, 10, 8, 10, 9, 10, 10, 10, 11, 10, 12, 10, 13, 10, 14, 10, 15, 10, 16, 10, 17, 10, 18, 10, 19, 10, 20, 10, 21, 9, 22, 8, 23, 7, 23, 6, 23, 5, 23, 4, 23, 3, 23, 2, 23, 1, 23, 0));
        path5.addTrafficLight(lights.get(5), 16, 14);

        Path path6 = new Path(getPoints(0, 11, 1, 11, 2, 11, 3, 11, 4, 11, 5, 11, 6, 11, 7, 11, 8, 11, 9, 11, 10, 11, 11, 11, 12, 11, 13, 11, 14, 11, 15, 11, 16, 11, 17, 11, 18, 11, 19, 11, 20, 11, 21, 11, 22, 11, 23, 11, 24, 11, 25, 11, 26, 11, 27, 11, 28, 11, 29, 11, 30, 11, 31, 11, 32, 11));
        path6.addTrafficLight(lights.get(6), 16, 14);

        Path path7 = new Path(getPoints(0, 12, 1, 12, 2, 12, 3, 12, 4, 12, 5, 12, 6, 12, 7, 12, 8, 12, 9, 12, 10, 12, 11, 12, 12, 12, 13, 12, 14, 12, 15, 12, 16, 12, 17, 12, 18, 12, 18, 13, 18, 14, 18, 15, 18, 16, 18, 17, 18, 18, 18, 19, 18, 20, 18, 21));
        path7.addTrafficLight(lights.get(7), 16, 14);
        //

        //east
        Path path8 = new Path(getPoints(21, 21, 21, 20, 21, 19, 21, 18, 21, 17, 21, 16, 21, 15, 21, 14, 21, 13, 21, 12, 21, 11, 21, 10, 20, 9, 19, 8, 18, 8, 17, 8, 16, 8, 15, 8, 14, 8, 13, 8, 12, 8, 11, 8, 10, 8, 9, 8, 8, 8, 7, 8, 6, 8, 5, 8, 4, 8, 3, 8, 2, 8, 1, 8, 0, 8));
        path8.addTrafficLight(lights.get(8), 20, 6, 8);

        Path path9 = new Path(getPoints(22, 21, 22, 20, 22, 19, 22, 18, 22, 17, 22, 16, 22, 15, 22, 14, 22, 13, 22, 12, 22, 11, 22, 10, 22, 9, 22, 8, 22, 7, 22, 6, 22, 5, 22, 4, 22, 3, 22, 2, 22, 1, 22, 0));
        path9.addTrafficLight(lights.get(9), 9, 6, 8);

        Path path10 = new Path(getPoints(23, 21, 23, 20, 23, 19, 23, 18, 23, 17, 23, 16, 23, 15, 23, 14, 23, 13, 23, 12, 23, 11, 23, 10, 23, 9, 23, 8, 23, 7, 23, 6, 23, 5, 23, 4, 23, 3, 23, 2, 23, 1, 23, 0));
        path10.addTrafficLight(lights.get(10), 9, 6, 8);

        Path path11 = new Path(getPoints(24, 21, 24, 20, 24, 19, 24, 18, 24, 17, 24, 16, 24, 15, 24, 14, 24, 13, 24, 12, 25, 11, 26, 11, 27, 11, 28, 11, 29, 11, 30, 11, 31, 11, 32, 11));
        path11.addTrafficLight(lights.get(11), 9, 6, 8);
        //

        //north
        Path path12 = new Path(getPoints(32, 7, 31, 7, 30, 7, 29, 7, 28, 7, 27, 7, 26, 7, 25, 7, 24, 7, 23, 7, 23, 6, 23, 5, 23, 4, 23, 3, 23, 2, 23, 1, 23, 0));
        path12.addTrafficLight(lights.get(14), 9, 11, 8);

        Path path13 = new Path(getPoints(32, 8, 31, 8, 30, 8, 29, 8, 28, 8, 27, 8, 26, 8, 25, 8, 24, 8, 23, 8, 22, 8, 21, 8, 20, 8, 19, 8, 18, 8, 17, 8, 16, 8, 15, 8, 14, 8, 13, 8, 12, 8, 11, 8, 10, 8, 9, 8, 8, 8, 7, 8, 6, 8, 5, 8, 4, 8, 3, 8, 2, 8, 1, 8, 0, 8));
        path13.addTrafficLight(lights.get(13), 9, 8);

        Path path14 = new Path(getPoints(32, 9, 31, 9, 30, 9, 29, 9, 28, 9, 27, 9, 26, 9, 25, 9, 24, 9, 23, 9, 22, 9, 21, 9, 20, 10, 19, 11, 19, 12, 19, 13, 19, 14, 19, 15, 19, 16, 19, 17, 19, 18, 19, 19, 19, 20, 19, 21));
        path14.addTrafficLight(lights.get(12), 9, 8);
        //

        //ventweg
        Path path15 = new Path(getPoints(14, 0, 14, 1, 14, 2, 14, 3, 14, 4, 14, 5, 13, 6, 12, 7, 11, 8, 10, 8, 9, 8, 8, 8, 7, 8, 6, 8, 5, 8, 4, 8, 3, 8, 2, 8, 1, 8, 0, 8));
        path15.addTrafficLight(lights.get(0), 8, 8);
        //

        //bus
        Path busPath1 = new BusPath(getPoints(16, 0, 16, 1, 16, 2, 16, 3, 16, 4, 16, 5, 16, 6, 16, 7, 16, 8, 16, 9, 16, 10, 17, 11, 17, 12, 17, 13, 17, 14, 17, 15, 17, 16, 17, 17, 17, 18, 17, 19, 17, 20, 17, 21), 4);
        busPath1.addTrafficLight(lights.get(15), 8, 5, 7);

        Path busPath2 = new BusPath(getPoints(25, 21, 25, 20, 25, 19, 25, 18, 25, 17, 25, 16, 25, 15, 25, 14, 25, 13, 24, 12, 23, 11, 23, 10, 23, 9, 23, 8, 23, 7, 23, 6, 23, 5, 23, 4, 23, 3, 23, 2, 23, 1, 23, 0), 1);
        busPath2.addTrafficLight(lights.get(16), 9, 3);

        Path busPath3 = new BusPath(getPoints(25, 21, 25, 20, 25, 19, 25, 18, 25, 17, 25, 16, 25, 15, 25, 14, 25, 13, 25, 12, 25, 11, 26, 11, 27, 11, 28, 11, 29, 11, 30, 11, 31, 11, 32, 11), 1);
        busPath3.addTrafficLight(lights.get(166), 9, 3);
        //

        //cycling
        Path cycPath1 = new CyclingPath(getPoints(14, 0, 14, 1, 14, 2, 14, 3, 14, 4, 14, 5, 14, 6, 14, 7, 14, 8, 14, 9, 14, 10, 14, 11, 14, 12, 14, 13, 14, 14, 14, 15, 14, 16, 14, 17, 14, 18, 14, 19, 14, 20, 14, 21));
        cycPath1.addTrafficLight(lights.get(19), 8, 8, 10);
        //opposite direction
        Path cycPath2 = new CyclingPath(getPoints(14, 21, 14, 20, 14, 19, 14, 18, 14, 17, 14, 16, 14, 15, 14, 14, 14, 13, 14, 12, 14, 11, 14, 10, 14, 9, 14, 8, 14, 7, 14, 6, 14, 5, 14, 4, 14, 3, 14, 2, 14, 1, 14, 0));
        cycPath2.addTrafficLight(lights.get(20), 9, 9, 13);

        Path cycPath3 = new CyclingPath(getPoints(0, 14, 1, 14, 2, 14, 3, 14, 4, 14, 5, 14, 6, 14, 7, 14, 8, 14, 9, 14, 10, 14, 11, 14, 12, 14, 13, 14, 14, 14, 14, 15, 15, 15, 16, 15, 17, 15, 18, 15, 19, 15, 20, 15, 21, 15, 22, 15, 23, 15, 24, 15, 25, 15, 26, 15, 27, 15, 28, 15, 29, 15, 30, 15, 31, 15, 32, 15));
        cycPath3.addTrafficLight(lights.get(22), 19, 18, 22);
        //opposite direction
        Path cycPath4 = new CyclingPath(getPoints(27, 21, 27, 20, 27, 19, 27, 18, 27, 17, 27, 16, 27, 15, 26, 15, 25, 15, 24, 15, 23, 15, 22, 15, 21, 15, 20, 15, 19, 15, 18, 15, 17, 15, 16, 15, 15, 15, 14, 15, 14, 14, 13, 14, 12, 14, 11, 14, 10, 14, 9, 14, 8, 14, 7, 14, 6, 14, 5, 14, 4, 14, 3, 14, 2, 14, 1, 14, 0, 14));
        cycPath4.addTrafficLight(lights.get(21), 9, 8, 14);

        Path cycPath5 = new CyclingPath(getPoints(32, 5, 31, 5, 30, 5, 29, 5, 28, 5, 27, 5, 26, 5, 25, 5, 24, 5, 23, 5, 22, 5, 21, 5, 20, 5, 19, 5, 18, 5, 17, 5, 16, 5, 15, 5, 14, 5, 14, 6, 14, 7, 14, 8, 14, 9, 14, 10, 14, 11, 14, 12, 14, 13, 14, 14, 14, 15, 14, 16, 14, 17, 14, 18, 14, 19, 14, 20, 14, 21));
        cycPath5.addTrafficLight(lights.get(17), 9, 9, 12);
        cycPath5.addTrafficLight(lights.get(19), 21, 21, 23);
        //

        //pedestrian
        Path pedPath1 = new PedPath(getPoints(13, 0, 13, 1, 13, 2, 13, 3, 13, 4, 13, 5, 13, 6, 13, 7, 13, 8, 13, 9, 13, 10, 13, 11, 13, 12, 13, 13, 13, 14, 13, 15, 13, 16, 13, 17, 13, 18, 13, 19, 13, 20, 13, 21));
        pedPath1.addTrafficLight(lights.get(28), 10, 8);
        pedPath1.addTrafficLight(lights.get(30), 13, 10);
        //opposite
        Path pedPath2 = new PedPath(getPoints(13, 21, 13, 20, 13, 19, 13, 18, 13, 17, 13, 16, 13, 15, 13, 14, 13, 13, 13, 12, 13, 11, 13, 10, 13, 9, 13, 8, 13, 7, 13, 6, 13, 5, 13, 4, 13, 3, 13, 2, 13, 1, 13, 0));
        pedPath2.addTrafficLight(lights.get(29), 13, 9);
        pedPath2.addTrafficLight(lights.get(27), 14, 13);

        Path pedPath3 = new PedPath(getPoints(0, 14, 1, 14, 2, 14, 3, 14, 4, 14, 5, 14, 6, 14, 7, 14, 8, 14, 9, 14, 10, 14, 11, 14, 12, 14, 13, 14, 14, 14, 14, 15, 15, 15, 16, 15, 17, 15, 18, 15, 19, 15, 20, 15, 21, 15, 22, 15, 23, 15, 24, 15, 25, 15, 26, 15, 27, 15, 28, 15, 29, 15, 30, 15, 31, 15, 32, 15));
        pedPath3.addTrafficLight(lights.get(31), 22, 18);
        pedPath3.addTrafficLight(lights.get(33), 22, 27);
        //opposite
        Path pedPath4 = new PedPath(getPoints(26, 21, 26, 20, 26, 19, 26, 18, 26, 17, 26, 16, 26, 15, 26, 15, 25, 15, 24, 15, 23, 15, 22, 15, 21, 15, 20, 15, 19, 15, 18, 15, 17, 15, 16, 15, 15, 15, 14, 15, 14, 14, 13, 14, 12, 14, 11, 14, 10, 14, 9, 14, 8, 14, 7, 14, 6, 14, 5, 14, 4, 14, 3, 14, 2, 14, 1, 14, 0, 14));
        pedPath4.addTrafficLight(lights.get(34), 14, 8);
        pedPath4.addTrafficLight(lights.get(32), 17, 14);

        Path pedPath5 = new PedPath(getPoints(32, 4, 31, 4, 30, 4, 29, 4, 28, 4, 27, 4, 26, 4, 25, 4, 24, 4, 23, 4, 22, 4, 21, 4, 20, 4, 19, 4, 18, 4, 17, 4, 16, 4, 15, 4, 14, 4, 14, 3, 14, 2, 14, 1, 14, 0));
        pedPath5.addTrafficLight(lights.get(23), 12, 9);
        pedPath5.addTrafficLight(lights.get(26), 17, 12);
        //opposite 
        Path pedPath6 = new PedPath(getPoints(14, 0, 14, 1, 14, 2, 14, 3, 14, 4, 15, 4, 16, 4, 17, 4, 18, 4, 19, 4, 20, 4, 21, 4, 22, 4, 23, 4, 24, 4, 25, 4, 26, 4, 27, 4, 28, 4, 29, 4, 30, 4, 31, 4, 32, 4));
        pedPath6.addTrafficLight(lights.get(25), 11, 6);
        pedPath6.addTrafficLight(lights.get(24), 14, 12);
        //

        paths = new Path[]{path1, path2, path3, path4, path5, path6, path7, path8, path9, path10, path11, path12, path13, path14, path15, cycPath1, cycPath2, cycPath3, cycPath4, cycPath5, busPath1, busPath2, busPath3, pedPath1, pedPath2, pedPath3, pedPath4, pedPath5, pedPath6};

        spawner = new VehicleSpawner(this);
    }

    private void addLight(TrafficLight l) {
        lights.put(l.getId(), l);
    }

    public void addRoadUser(RoadUser v, Path path) {
        roadUsers.add(v);
        path.addUser(v);
    }

    public void updateLogic() {
        for (Iterator<RoadUser> iterator = roadUsers.iterator(); iterator.hasNext();) {
            RoadUser v = iterator.next();
            v.update(System.currentTimeMillis());
            if (v.getStatus() == RoadUser.Status.OFF_SCREEN) {
                iterator.remove();
            }
        }
        spawner.update();
    }

    public void draw(Graphics g, int width, int height) {

        int CELLWIDTH = width / columns;
        int CELLHEIGHT = height / rows;

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 10));

        drawRoads(g2, CELLWIDTH, CELLHEIGHT);
        drawPaths(g2, CELLWIDTH, CELLHEIGHT);
        drawVehicles(g2, CELLWIDTH, CELLHEIGHT);
        drawLights(g2, CELLWIDTH, CELLHEIGHT);
    }

    private void drawPaths(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        for (Path p : paths) {
            p.draw(g2, CELLWIDTH, CELLHEIGHT);
        }
    }

    private void drawRoads(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        final char BUSTILE = '2';
        final char CYCLINGTILE = '0';
        final char CARTILE = '1';
        final char CRYSTALICTILE = 'c';

        for (int y = 0; y < rows; y++) {
            char[] row = grid[y];

            for (int i = 0; i < columns; i++) {

                switch (row[i]) {

                    case CRYSTALICTILE:
                        g2.setColor(Color.blue);
                        g2.fillRect(i * CELLWIDTH, y * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                        break;
                    case CYCLINGTILE:
                        g2.setColor(Color.orange);
                        g2.fillRect(i * CELLWIDTH, y * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                        break;
                    case BUSTILE:
                        g2.setColor(Color.GREEN);
                        g2.fillRect(i * CELLWIDTH, y * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                        break;
                    case CARTILE:
                        g2.setColor(Color.gray);
                        g2.fillRect(i * CELLWIDTH, y * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                        break;

                }
                g2.setColor(Color.BLACK);
                g2.drawRect(i * CELLWIDTH, y * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
            }
        }
    }

    private void drawLights(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        for (TrafficLight light : lights.values()) {
            light.draw(g2, CELLWIDTH, CELLHEIGHT);
        }
    }

    private synchronized void drawVehicles(Graphics2D g2, int CELLWIDTH, int CELLHEIGHT) {
        g2.setColor(Color.BLACK);
        for (RoadUser v : roadUsers) {
            v.draw(g2, CELLWIDTH, CELLHEIGHT);
            g2.setColor(Color.BLACK);
        }
    }

    public void setPathVisibility(int path, boolean v) {
        paths[path].setVisibile(v);
    }

    public boolean isBusLight(int id) {
        return id == 15 || id == 16;
    }

    public void setLightState(int id, int status) {
        if (status < 0) { //safeguard, should never happen (server should only send between 0 and 5)
            return;
        }

        if (isBusLight(id)) {
            for (BusLight l : getBusLights(id, status)) {
                l.setStatus(status > 2 ? TrafficLight.Status.GREEN : TrafficLight.Status.values()[status]);
            }
        } else if (lights.containsKey(id)) { //status > 2 should not happen
            lights.get(id).setStatus(status > 2 ? TrafficLight.Status.GREEN : TrafficLight.Status.values()[status]);
        }
    }

    public void setTLNumberVisibility(boolean v) {
        TrafficLight.setIdVisibility(v);
    }
}
