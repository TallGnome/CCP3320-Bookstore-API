package org.city.bookstore.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";  
	
	static final String HOST = "";
	
	//  Database credentials
	static final String USR = "jfkhvfdktprvrd";
	static final String PASS = "7b1c1c947765931698475d6b35d7a2ac3df4e83b7214e78e08089e31b138d5bc";
	static final String DB_URL = "jdbc:postgresql://ec2-54-75-249-162.eu-west-1.compute.amazonaws.com:5432/d2s4fbjdbqtgk7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	private static Database instance = null;

	private Connection conn = null;
	

	protected Database() throws ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
	}

	public static Database getInstance() throws ClassNotFoundException {
		if(instance == null) {
			instance = new Database();
		}
		return instance;
	}

	public Connection connect(String action) throws SQLException{
		
		System.out.println("Connecting to database for "+ action +"...");
		conn = DriverManager.getConnection(DB_URL, USR, PASS);
		return conn;
		
	}
}