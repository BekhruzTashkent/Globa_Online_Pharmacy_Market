package com.example.demo6;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {

    static List<ClientControl> clients = new ArrayList<ClientControl>();

    public static void main(String[] args) {
        try
        {
            // creating server and giving it a port
            ServerSocket serverSocket = new ServerSocket(3022);
            System.out.println("Server started on " + new Date());

            // to connect clients
            int clientNumber = 1;
            while (true) {
                Socket client = serverSocket.accept();
                // getting info about client and displaying it
                InetAddress inetAddress = client.getInetAddress();
                System.out.println("\nClient " + clientNumber + " connected; IP: " + inetAddress.getHostAddress() + " ; Host name: " + inetAddress.getHostName());
                ClientControl clientControl;
                // starting thread for client
                Thread clientThread = clientControl = new ClientControl(client);
                clients.add(clientControl);
                clientThread.start();
                clientNumber++;
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    // method to remove users from List
    public static void removeUser()
    {
        for(int i = 0; i < clients.size(); i++)
        {
            if(!clients.get(i).clientSocket.isConnected())
            {
                clients.get(i).interrupt();
                clients.remove(i);
            }
        }
    }
}

// class for many clients to work simultaneously
class ClientControl extends Thread{
    DataInputStream inputOfClient;
    DataOutputStream outputForClient;
    Socket clientSocket;

    // getting information about User from Server class
    public ClientControl(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        try {
            inputOfClient = new DataInputStream(clientSocket.getInputStream());
            outputForClient = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // overriding method run
    @Override
    public void run()
    {
        String operation;
        while (true)
        {
            try
            {
                operation = inputOfClient.readUTF();   // to know what operation User wants to implement

                // To add new medical into Database
                if (operation.equals("write"))
                {
                    String name = inputOfClient.readUTF();
                    double price = inputOfClient.readDouble();
                    int amount = inputOfClient.readInt();
                    // connecting to Database
                    DataBase dataBase = new DataBase();

                    // to add medical only if it does NOT exist in Database
                    if(!dataBase.isExist(name))
                    {
                        dataBase.insert(name, price, amount);
                        outputForClient.writeUTF(name + " added successfully");
                    }
                    else
                    {
                        outputForClient.writeUTF(name + " already exists");
                    }

                    // disconnecting from dataBase
                    dataBase.disconnect();
                }

                // To delete existing medical from Database
                else if (operation.equals("delete"))
                {
                    String name = inputOfClient.readUTF();
                    // connecting to Database
                    DataBase dataBase = new DataBase();

                    // Delete operation can be implemented only if given medical exists
                    if(dataBase.isExist(name))
                    {
                        dataBase.delete(name);
                        outputForClient.writeUTF(name + " deleted successfully");
                    }
                    else
                    {
                        outputForClient.writeUTF(name + " does NOT exist");
                    }

                    // disconnecting from dataBase
                    dataBase.disconnect();
                }

                // To update information about existing medical in Database
                else if (operation.equals("update"))
                {
                    String name = inputOfClient.readUTF();
                    double price = inputOfClient.readDouble();
                    int amount = inputOfClient.readInt();
                    // connecting to Database
                    DataBase dataBase = new DataBase();
                    // Update operation can be implemented only if given medical exists
                    if(dataBase.isExist(name))
                    {
                        dataBase.update(name, price, amount);
                        outputForClient.writeUTF(name + " updated successfully");
                    }
                    else
                    {
                        outputForClient.writeUTF(name + " does NOT exist");
                    }

                    // disconnecting from dataBase
                    dataBase.disconnect();
                }

                // To get price of particular medical in Database
                else if (operation.equals("readPrice"))
                {
                    String name = inputOfClient.readUTF();
                    // connecting to Database
                    DataBase dataBase = new DataBase();
                    double price = dataBase.selectPrice(name);
                    // disconnecting from dataBase
                    dataBase.disconnect();
                    // sending result to User
                    outputForClient.writeDouble(price);
                }

                // To get amount left of particular medication in Database
                else if (operation.equals("readAmount"))
                {
                    String name = inputOfClient.readUTF();
                    // connecting to Database
                    DataBase dataBase = new DataBase();
                    int amount = dataBase.selectAmount(name);
                    // disconnecting from dataBase
                    dataBase.disconnect();
                    // sending result to User
                    outputForClient.writeInt(amount);
                }

                // To find all medicals from Database which contain letters entered by User
                else if (operation.equals("readSearchResults"))
                {
                    String name = inputOfClient.readUTF();
                    // connecting to Database
                    DataBase dataBase = new DataBase();
                    String searchingNames = dataBase.search(name);
                    // disconnecting from dataBase
                    dataBase.disconnect();
                    // sending result to User
                    outputForClient.writeUTF(searchingNames);
                }

                // To subtract ordered amount of medicals from existing amount in Database
                else if (operation.equals("minusAmount"))
                {
                    String name = inputOfClient.readUTF();
                    int minusAmount = inputOfClient.readInt();
                    // connecting to Database
                    DataBase dataBase = new DataBase();
                    dataBase.minusAmount(name, minusAmount);
                    // disconnecting from dataBase
                    dataBase.disconnect();
                    // sending result to User
                    outputForClient.writeUTF("Ordered successfully!");
                }

                // Otherwise, if User gave wrong command
                else
                {
                    System.out.println("!!! Wrong Operation Required");
                }
            }
            catch (EOFException exception)
            {
                try
                {
                    Server.removeUser();
                    clientSocket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
            catch (SocketException socketException)
            {
                try
                {
                    clientSocket.close();
                    Server.removeUser();
                    break;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            inputOfClient.close();
            outputForClient.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
