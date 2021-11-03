package kruispunt;

import Server.Server;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Main
{
    private static boolean running = true;
    private static World world;
    private static Thread mainThread;
    private static Connection connection;
    private static GUI userInterface;
    private final static int UPDATE_INTERVAL = 200;

    public static GUI getUserInterface() {
        return userInterface;
    }

    public static World getWorld() {
        return world;
    }

    public static Connection getConnection() {
        return connection;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {

        world = new World();
        world.load();

        final MyLabel label = new MyLabel(getWorld());
        userInterface = new GUI(label);
        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                running = false;
                userInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        };
        userInterface.addWindowListener(windowAdapter);
        userInterface.setVisible(true);

        connection = new Connection();

        //connect();
        mainThread = new Thread() {
            @Override
            public void run() {
                while (running) {
                    world.updateLogic();
                    label.repaint();
                    try {
                        Thread.sleep(UPDATE_INTERVAL);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyLabel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        mainThread.start();
    }
}
