package selimsqldb.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BuildTableSSqlDb {

	public boolean buildTablesAndIndexes(Connection connection) throws SQLException {
		System.out.println("-SelimSql buildTablesAndIndexes ------");
		Statement statement = null;
		try {
			statement = connection.createStatement();
			System.out.println("connection.statement okay.");

			String sql = "CREATE TABLE Product"
						+ " (Id INTEGER NOT NULL,"
						+ " Name VARCHAR(20) NOT NULL,"
						+ " Price DECIMAL(15, 2) NOT NULL,"
						+ " ProductDate DATE NOT NULL,"
						+ " Status CHAR NOT NULL,"
						+ " Comment LONGTEXT,"
						+ " Photo BLOB)";

			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();

			sql = "CREATE UNIQUE INDEX ProductPk ON Product(Id)";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();


			sql = "Create Table Customer"
				+ " (Id INTEGER NOT NULL,"
				+ " EntryDate DATE NOT NULL,"
				+ " Name VARCHAR(20) NOT NULL,"
				+ " Surname VARCHAR(20) NOT NULL,"
				+ " Sex CHAR NOT NULL,"
				+ " Job VARCHAR(30),"
				+ " Email VARCHAR(30))";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();

			sql = "CREATE UNIQUE INDEX CustomerPk ON Customer(Id)";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();

			sql = "CREATE INDEX CustomerNameIdx ON Customer(Name, Surname)";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();


			sql = "Create Table Order"
				+ " (Id INTEGER NOT NULL,"
				+ " CustomerId INTEGER NOT NULL,"
				+ " ProductId INTEGER NOT NULL,"
				+ " OrderDate DATE NOT NULL,"
				+ " OrderAmount SMALLINT NOT NULL,"
				+ " UnitPrice DECIMAL(15, 2) NOT NULL,"
				+ " PaymentType VARCHAR(10) NOT NULL,"
				+ " Status VARCHAR(10) NOT NULL)";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();

			sql = "CREATE UNIQUE INDEX OrderPk ON Order(Id)";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();

			sql = "CREATE INDEX OrderCusIdx ON Order(CustomerId)";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();

			sql = "CREATE INDEX OrderDateIdx ON Order(OrderDate)";
			statement.executeUpdate(sql);
			System.out.println("-Statement Sql executed:");
			System.out.println(sql);
			System.out.println();
		}
		finally {
			if (statement!=null) {
				statement.close();
				System.out.println("connection.statement closed.");
			}
		}
		return (statement!=null);
	}
}
