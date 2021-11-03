/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kruispunt;

import Server.Server;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Len
 */
public class Connection {

    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;
    private PrintWriter pw;
    private BufferedReader inReader;
    private Thread thread;

    public void stop() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getIP() {
        if (socket != null) {
            return socket.getInetAddress().getHostAddress();
        }
        return "";
    }

    public String getPort() {
        if (socket != null) {
            return Integer.toString(socket.getPort());
        }
        return "";
    }

    public void start(String ip, int port) throws IOException {
        stop();
        System.out.println("Trying to connect");
        socket = new Socket(ip, port);
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        pw = new PrintWriter(os);
        inReader = new BufferedReader(new InputStreamReader(is));

        thread = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        read();
                    } catch (JSONException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }
                }
                Main.getUserInterface().setConnectionIndication(false);
                System.out.println("Lost connection with server");
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
                socket = null;
            }
        };
        System.out.println("Connected to server");
        Main.getUserInterface().setConnectionIndication(true);
        thread.start();
    }

    private void read() throws JSONException, IOException {
        String line = inReader.readLine();
        System.out.println("received line: " + line);
        JSONArray lightsArray = new JSONObject(line).getJSONArray("stoplichten");
        for (int i = 0; i < lightsArray.length(); i++) {
            JSONObject light = lightsArray.getJSONObject(i);
            int id = light.getInt("id");
            int status = light.getInt("status");
            System.out.println("Id: " + id + ", status: " + status);
            Main.getWorld().setLightState(id, status);
        }
    }

    public void send(JSONObject jsonObject) {
        String s = jsonObject.toString();
        if (socket != null) {
            pw.println(s);
            pw.flush();
        }
        System.out.println(s);
    }

    public void sendStatus(int id, boolean status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("banen", new JSONArray().put(new JSONObject().put("id", id).put("bezet", status)));
            send(jsonObject);
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendBusStatus(int id, int line, boolean status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("busbanen", new JSONArray().put(new JSONObject().put("id", id).put("eerstvolgendelijn", line).put("bezet", status)));
            send(jsonObject);
           
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
