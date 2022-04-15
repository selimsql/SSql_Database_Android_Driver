package selimsqldb.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NewSSqlDb {

	public Connection newDb(String dbName) throws SQLException {
		System.out.println("-SelimSql NewDB Connection Testing ------");
		try {
			Class.forName(DbConstant.SELIMSQL_JDBC_CLASS);
		}
		catch(ClassNotFoundException e) {
			throw new SQLException("ClassNotFoundException; No driver for SelimSql!");
		}
		System.out.println("SelimSql JDBC Driver Registered!");

		try {
			//ex: jdbc:selimsql:fileNewDatabase:MyDb
			String url = DbConstant.SELIMSQL_JDBC_URL_HEADER + DbConstant.SELIMSQL_DB_TYPE_FILENEWDATABASE + ":" + dbName;

			//If you want to build a new database on ssql database server, use this url:
			//Example, jdbc:selimsql://192.168.2.3:9933/fileNewDatabase:MyDb
			//String url = DbConstant.SELIMSQL_JDBC_URL_HEADER + DbConstant.SELIMSQL_DB_TYPE_HOST + "SSqlDbServerIp:9933/" + DbConstant.SELIMSQL_DB_TYPE_FILENEWDATABASE + ":" + dbName;

			String user = DbConstant.USER_ADMIN;
			String pass = null; //No password.
            Connection con = DriverManager.getConnection(url, user, pass);

            System.out.println("Connection succesfull.");
            return con;
		}
		catch(SQLException ex) {
			System.out.println("Error while new connection:" + ex.getMessage());
			throw ex;
		}
        catch(Throwable ext) {
            System.out.println("Unknown Error Type:" + ext.getClass().getSimpleName() + "; " + ext.getMessage());
            throw new SQLException(ext);
            //! throw ext;
        }
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
