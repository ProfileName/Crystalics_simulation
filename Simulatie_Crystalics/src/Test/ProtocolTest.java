/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Len
 */
public class ProtocolTest {

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException, JSONException {

        //connection to the server
        TestConnection connection = new TestConnection();

        //server addres
        final String IP = "192.168.100.118";
        final int PORT = 5005;

        //connect to server
        connection.start(IP, PORT);

        //start sending to the server
        sendSingles(connection);
        sendCollection(connection);
    }

    /**
     * sends 1 JsonString over a socket, containing 32 road fields.
     *
     * @param con
     * @throws JSONException
     */
    private static void sendCollection(TestConnection con) throws JSONException {
        JSONArray roads = new JSONArray();
        Random random = new Random();

        //add 32 different roads to array with random value for bezet
        for (int i = 1; i < 33; i++) {
            roads.put(new JSONObject().put("id", i).put("bezet", (random.nextInt(2) == 1)));
        }
        System.out.println("trying to send messages");
        //send object after adding the road array to the root
        con.send(new JSONObject().put("banen", roads));
        System.out.println("DONE: sent 1 message containing 32 roads");
    }

    /**
     * sends 32 JsonStrings over a socket, each containing 1 road field.
     *
     * @param con
     * @throws JSONException
     * @param con
     * @throws JSONException
     */
    private static void sendSingles(TestConnection con) throws JSONException {
        Random random = new Random();

        for (int i = 1; i < 33; i++) {
            con.send(new JSONObject().put("banen", new JSONArray().put(new JSONObject().put("id", i).put("bezet", (random.nextInt(2) == 1)))));
        }
        System.out.println("DONE: sent 32 messages, each containing 1 road");
    }
}
