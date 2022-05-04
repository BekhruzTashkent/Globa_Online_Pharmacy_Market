package com.example.demo6;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.*;

// User class to connect HelloController with Server
public class User {

    private DataInputStream inputOfServer;
    private DataOutputStream outputForServer;
    // constructor to connect to Server
    public User() {
        try
        {
            Socket socket = new Socket("localhost", 3022);   // creating socket for client
            inputOfServer = new DataInputStream(socket.getInputStream());    // to get data from server
            outputForServer = new DataOutputStream(socket.getOutputStream());    // to send data to server
        }

        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    // To add new medical into Database through Server
    public String write(String name, double price, int amount)
    {
        String status = "";
        try {
            outputForServer.writeUTF("write");
            outputForServer.writeUTF(name);
            outputForServer.writeDouble(price);
            outputForServer.writeInt(amount);
            status = inputOfServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    // To delete existing medical from Database through Server
    public String delete(String name)
    {
        String status = "";
        try {
            outputForServer.writeUTF("delete");
            outputForServer.writeUTF(name);
            status = inputOfServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    // To update information about existing medical in Database through Server
    public String update(String name, double price, int amount)
    {
        String status = "";
        try {
            outputForServer.writeUTF("update");
            outputForServer.writeUTF(name);
            outputForServer.writeDouble(price);
            outputForServer.writeInt(amount);
            status = inputOfServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    // To get price of particular medical in Database through Server
    public double readPrice(String name)
    {
        double price = 0;
        try {
            outputForServer.writeUTF("readPrice");
            outputForServer.writeUTF(name);
            price = inputOfServer.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return price;
    }

    // To get amount left of particular medication in Database through Server
    public int readAmount(String name)
    {
        int amount = 0;
        try {
            outputForServer.writeUTF("readAmount");
            outputForServer.writeUTF(name);
            amount = inputOfServer.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return amount;
    }

    // To subtract ordered amount of medicals from existing amount in Database through Server
    public String minusAmount(String name, int minusAmount)
    {
        String result = "";
        try {
            outputForServer.writeUTF("minusAmount");
            outputForServer.writeUTF(name);
            outputForServer.writeInt(minusAmount);
            result = inputOfServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // To find all medicals from Database through Server which contain letters entered by User and return their List
    public List<String> readSearchResults(String name)
    {
        String names = "";
        try {
            outputForServer.writeUTF("readSearchResults");
            outputForServer.writeUTF(name);
            names = inputOfServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(names);
        List<String> searchingNames = new ArrayList<String>(Arrays.asList(names.split(",")));
        return searchingNames;
    }
}
