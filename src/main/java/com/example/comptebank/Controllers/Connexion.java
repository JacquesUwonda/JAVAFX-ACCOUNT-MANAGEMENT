package com.example.comptebank.Controllers;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
	    public static Connection getConnection() {
	        Connection connection = null;
	        try {
	            String url = "jdbc:mysql://localhost:3306/jdbc";
	            String user = "root";
	            String password = "";
	            connection = DriverManager.getConnection(url, user, password);
	        } catch (SQLException e) {
	            System.out.println("Error: "+e.getMessage());;
	        }
	        return connection;
	    }
}
