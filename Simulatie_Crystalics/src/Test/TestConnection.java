/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

/**
 *
 * @author Len
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class TestConnection {

    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;
    private PrintWriter pw;
    private BufferedReader inReader;
    private Thread thread;
    
    public int readCount;

    public void stop() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(TestConnection.class.getName()).log(Level.SEVERE, null, ex);
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
      //  stop();
        System.out.println("trying to connect to server");
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
                        Logger.getLogger(TestConnection.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TestConnection.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }
                }
                System.out.println("Lost connection with server");
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(TestConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                socket = null;
            }
        };
        System.out.println("Connected to server");
        thread.start();
    }

    private void read() throws JSONException, IOException {
        JSONArray lightsArray = new JSONObject(inReader.readLine()).getJSONArray("stoplichten");
        for (int i = 0; i < lightsArray.length(); i++) {
            JSONObject light = lightsArray.getJSONObject(i);
            int id = light.getInt("id");
            int status = light.getInt("status");
            System.out.println("read message -> id: " + id + ", status: " + status);
        }
        System.out.println("total received messages: " + ++readCount);
    }

    public void send(JSONObject jsonObject) {
        if (socket != null) {
            pw.println(jsonObject.toString());
            pw.flush();
        }
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
    
    
    public void sendBusStatus(int id, int lijn, boolean status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("busbanen", new JSONArray().put(new JSONObject().put("id", id).put("eerstvolgendelijn",lijn).put("bezet", status)));
            send(jsonObject);
        } catch (JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

