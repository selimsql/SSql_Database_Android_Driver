package selimsqldb.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectSSqlDb {

	public Connection connectDb(String dbName) {
		System.out.println("-SelimSql Connection Testing ------");
		try {
			Class.forName(DbConstant.SELIMSQL_JDBC_CLASS);
		}
		catch(ClassNotFoundException ex) {
			//Log.v(null, msg);
			System.out.println("ClassNotFoundException; No driver for SelimSql!");
			return null;
		}
		System.out.println("SelimSql JDBC Driver Registered!");

		Connection con = null;
		try {
			//ex: jdbc:selimsql:file:MyDb
			String url = DbConstant.SELIMSQL_JDBC_URL_HEADER + DbConstant.SELIMSQL_DB_TYPE_FILE + ":" + dbName;

			//If you want to connect to a ssql database server, use this url:
			//Example, jdbc:selimsql://192.168.2.3:9933/MyDb
			//String url = DbConstant.SELIMSQL_JDBC_URL_HEADER + DbConstant.SELIMSQL_DB_TYPE_HOST + "SSqlDbServerIp:9933/" + dbName;

			String user = DbConstant.USER_ADMIN;
			String pass = null; //No password.
			con = DriverManager.getConnection(url, user, pass);
		}
		catch(SQLException ex) {
			System.out.println("Error while connection:" + ex.getMessage());
		}
		catch(Throwable ext) {
			System.out.println("Unknown Error Type:" + ext.getClass().getSimpleName() + "; " + ext.getMessage());
		}

		if (con != null)
			System.out.println("Connection succesfull.");
		else
			System.out.println("Connection failed!");

		return con;
	}

	public boolean closeDb(Connection con) {
		System.out.println("-SelimSql Closing ------");
		if (con==null) {
			System.out.println("No connection for SelimSql");
			return false;
		}

		try {
			if (con.isClosed()) {
				System.out.println("Connection is already closed!");
				return false;
			}

			con.close();
			System.out.println("Connection closed.");
			return true;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
