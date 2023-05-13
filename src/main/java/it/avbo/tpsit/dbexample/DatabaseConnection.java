package it.avbo.tpsit.dbexample;

import java.sql.*;

public class DatabaseConnection {
	private String dbDriver;
	private String dbURL;
	private String dbUser;
	private String dbPassword;
	
	public DatabaseConnection() {
	}
	
	public DatabaseConnection(String dbDriver, String dbURL) {
		this(dbDriver, dbURL, "", "");
	}
	
	public DatabaseConnection(String dbDriver, String dbURL, String dbUser, String dbPassword) {
		this();
		this.dbDriver = dbDriver;
		this.dbURL = dbURL;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	public Connection initializeConnection() 
			throws SQLException, ClassNotFoundException {
		//Inizializza la classe che implementa il driver del database scelto
		Class.forName(dbDriver);
		Connection con;
		//Se non è stato passato un nome utente
		if (dbUser == null || dbUser.isBlank())
			//Crea una connessione senza nome utente
			con = DriverManager.getConnection(dbURL);
		else
			//Altrimenti la crea utilizzando il nome utente e la password indicati
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		//Restituisce la connessione creata
		return con;
	}

	protected String getDbDriver() {
		return dbDriver;
	}

	protected void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	protected String getDbURL() {
		return dbURL;
	}

	protected void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	protected String getDbUser() {
		return dbUser;
	}

	protected void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	protected String getDbPassword() {
		return dbPassword;
	}

	protected void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
}
