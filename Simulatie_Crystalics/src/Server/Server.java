package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Server
{
    private Thread thread;
    private ServerSocket socket;
    private Socket client;

    private DataInputStream in;
    private DataOutputStream out;

    private PrintWriter pw;
    private BufferedReader inReader;

    public static Server s;

    public static void main(String[] args) throws IOException {
        s = new Server();
        s.start(6067);

        GUI gui = new GUI();
        gui.setVisible(true);
    }

    public String getPort() {
        if (socket != null) {
            return Integer.toString(socket.getLocalPort());
        }
        return "";
    }

    public void start(final int port) throws IOException {
        socket = new ServerSocket(port);
        final Server server = this;
        thread = new Thread() {


            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        client = socket.accept();
                        System.out.println("Server: connected new client");

                        in = new DataInputStream(client.getInputStream());
                        out = new DataOutputStream(client.getOutputStream());
                        inReader = new BufferedReader(new InputStreamReader(in));
                        pw = new PrintWriter(out);

                        while (!Thread.currentThread().isInterrupted()) {
                            try {
                                read();
                            } catch (JSONException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }

                }
                try {
                    server.start(port);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        thread.start();
    }

    private void read() throws JSONException, IOException, NullPointerException {
        JSONObject json = new JSONObject(inReader.readLine());
        System.out.println(json.toString());
    }

    public void stop() throws IOException {
        if (thread != null) {
            socket.close();
            thread.interrupt();
            thread = null;
        }
    }

    public void setLight(int id, int status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stoplichten", new JSONArray().put(new JSONObject().put("id", id).put("status", status)));
            send(jsonObject);
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(JSONObject jsonObject) {
        pw.println(jsonObject.toString());
        pw.flush();
    }

}
