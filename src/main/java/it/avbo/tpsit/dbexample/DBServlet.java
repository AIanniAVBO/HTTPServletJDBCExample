package it.avbo.tpsit.dbexample;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class DBServlet
 */
public class DBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Oggetto che serve a gestire le connessioni col database
	 */
	private DatabaseConnection dbConnection;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DBServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * Questo metodo di init viene chiamato una sola volta all'avvio della servlet
	 * lo scopo è quello di inizializzare la classe per la gestione del database
	 * e creare la tabella con i dati se necessario
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		//Chiama il metodo base
		super.init(config);
		//Ottiene il context, utile per poter leggere i parametri dal web.xml
		var context = getServletContext();
		//Legge i parametri di inizializzazione del database, vedere il file web.xml per
		//	vedere come sono stati inseriti al suo interno
		var dbDriver = context.getInitParameter("dbDriver");
		var dbURL = context.getInitParameter("dbURL");
		var dbUser = context.getInitParameter("dbUser");
		var dbPassword = context.getInitParameter("dbPassword");
		//Inizializza l'oggetto che si occupa di creare le connessioni col database
		dbConnection = new DatabaseConnection(dbDriver, dbURL, dbUser, dbPassword);
		//Inizializza una connessione
		try (	var conn = dbConnection.initializeConnection();
				//Inizializza uno statement
				var statement = conn.createStatement()) {
			//Se non esiste crea la tabella person (potrebbe essere necessario adattarla al dialetto
			//	del database utilizzato)
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name STRING)");
			//Aggiunge due righe alla tabella
			statement.executeUpdate("INSERT INTO person(name) VALUES('leo')");
			statement.executeUpdate("INSERT INTO person(name) VALUES('yui')");
		} catch (ClassNotFoundException | SQLException e) {
			//Nel caso ci siano problemi fa in modo che venga stampato lo stack trace nel browser web
			throw new ServletException(e);
		} 
	}
	
	/**
	 * metodo richiamato quando la servlet riceve una richiesta di tipo GET
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Crea una array json
		JsonArray jarr = new JsonArray();
		//Crea la connessione e uno statement
		try (	var conn = dbConnection.initializeConnection();
				var statement = conn.createStatement()) {
			//Imposta il timeout a 30 secondi
			statement.setQueryTimeout(30); 
			//Effettua una query leggendo il contenuto di tutta la tabella person
			ResultSet rs = statement.executeQuery("select * from person");
			//Per ogni riga letta
			while (rs.next()) {
				//Crea un oggetto json
				JsonObject jobj = new JsonObject();
				//Scrive il nome letto dal db
				jobj.addProperty("name", rs.getString("name"));
				//Scrive l'id letto dal db
				jobj.addProperty("id", rs.getString("id"));
				//Aggiunge l'oggetto json creato dentro l'array json creato in
				//	precedenza
				jarr.add(jobj);
			}
		} catch (SQLException | ClassNotFoundException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			response.getWriter().append(e.getMessage());
		}
		//Scrive l'header della risposta
		response.addHeader("Access-Control-Allow-Origin", "*");
		//Indica il tipo di file inviato
		response.setContentType("application/json");
		//Converte in stringa l'array json
		response.getWriter().append(jarr.toString());

	}

	/**
	 * Metodo richiamato quando si riceva una richiesta di tipo POST
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
