package com.example.demo6;

import java.sql.*;

public class DataBase {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/med";
    private static final String user = "root";
    private static final String password = "Bexruz1211";
    private Connection connection;

    // To connect to MySQL database when object of DataBase created using constructor
    public DataBase()
    {
        try {
            // creating connection to database
            connection = DriverManager.getConnection(url, user, password);
            if(!connection.isClosed())
            {
                System.out.println("Database is connected");
            }

            if(connection.isClosed())
            {
                System.out.println("Database is disconnected");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // To disconnect from database when there is no need for it
    public void disconnect()
    {
        try {
            // disconnecting from database
            connection.close();
            if(connection.isClosed())
            {
                System.out.println("Database is disconnected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // To insert data to DataBase
    public void insert(String name, Double price, int amount)
    {
        try (Statement statement = connection.createStatement()){
            String forStatement = "INSERT INTO medicals(Name, Price, Amount) VALUES(" + "'" + name + "'" + "," + price + "," + amount + ")";
            statement.execute(forStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // To delete data from Database
    public void delete(String name)
    {
        try (Statement statement = connection.createStatement()){
            String forStatement = "Delete from medicals where Name = " + "'" + name + "'" +";";
            int count = statement.executeUpdate(forStatement);
            System.out.println("Number: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // To update information about particular medical in Database
    public void update(String name, Double price, int amount)
    {
        try (Statement statement = connection.createStatement()){
            String forStatement = "UPDATE medicals SET Price = " + price + ", Amount = " + amount + " where Name = " + "'" + name + "'" +";";
            int count = statement.executeUpdate(forStatement);
            System.out.println("Number: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // To get price of particular medical from Database
    public double selectPrice(String name)
    {
        double price = 0;
        try (Statement statement = connection.createStatement()){
            String forStatement = "Select Price from medicals where Name = " + "'" + name + "'";
            ResultSet resultSet = statement.executeQuery(forStatement);
            while(resultSet.next())
            {
                price = resultSet.getDouble("Price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    // To get amount of particular medical from Database
    public int selectAmount(String name)
    {
        int amount = 0;
        try (Statement statement = connection.createStatement()){
            String forStatement = "Select Amount from medicals where Name = " + "'" + name + "'";
            ResultSet resultSet = statement.executeQuery(forStatement);
            while(resultSet.next())
            {
                amount = resultSet.getInt("Amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    // To subtract ordered amount of medicals from existing amount in Database
    public void minusAmount(String name, int minusAmount)
    {
        int amount = 0;
        try (Statement statement = connection.createStatement()){
            String forStatement = "Select Amount from medicals where Name = " + "'" + name + "'";
            ResultSet resultSet = statement.executeQuery(forStatement);
            while(resultSet.next())
            {
                amount = resultSet.getInt("Amount");
            }
            amount = amount - minusAmount;
            String forStatement2 = "UPDATE medicals SET Amount = " + amount + " where Name = " + "'" + name + "'" +";";
            int count = statement.executeUpdate(forStatement2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // To find all medicals from Database which contain letters entered by User
    public String search(String name)
    {
        //List<String> searchingNames = new ArrayList<>();
        String searchingNames = "";
        int numberOfResults = 0;
        try (Statement statement = connection.createStatement()){
            String forStatement = "Select * from medicals";
            ResultSet resultSet = statement.executeQuery(forStatement);

            while(resultSet.next())
            {
                // checking whether resultSet contains name entered by User
                boolean doesContain = resultSet.getString("Name").toLowerCase().contains(name.toLowerCase());
                // if it contains then writing all appropriate medicals names into String
                if(doesContain)
                {
                    System.out.println(resultSet.getString("Name"));
                    if(numberOfResults == 0)
                    {
                        searchingNames = resultSet.getString("Name");
                    }
                    else
                    {
                        searchingNames = searchingNames + "," + resultSet.getString("Name");
                    }
                    //searchingNames.add(resultSet.getString("Name"));
                    numberOfResults++;
                }
            }

            System.out.println("\n" + numberOfResults + " results found for " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchingNames;
    }

    // To check whether particular medical is already existing in Database or not
    public boolean isExist(String name)
    {
        boolean exist = false;
        try (Statement statement = connection.createStatement()){
            String forStatement = "Select * from medicals where Name = " + "'" + name + "';";
            ResultSet resultSet = statement.executeQuery(forStatement);
            while(resultSet.next())
            {
                if(name.equals(resultSet.getString("Name")))
                {
                    exist = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }
}
