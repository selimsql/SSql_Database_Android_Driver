package selimsqldb.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectRecSSqlDb {

	public int selectProductRecords(Connection connection) throws SQLException {
		System.out.println();
		System.out.println("-SelimSql selectProductRecords operations ------");
		Statement statement = null;
		int row = 0;
		try {
			statement = connection.createStatement();
			System.out.println("connection.statement okay.");

			String sql = "select * from Product where Id > 0";
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next())
			{
				row++;
				int recId = resultSet.getInt("Id");
				System.out.print(row + ". ProductId:" + recId);
				System.out.print(", Name:" + resultSet.getString("Name"));
				System.out.print(", Price:" + resultSet.getBigDecimal("Price"));
				System.out.print(", ProductDate:" + resultSet.getDate("ProductDate"));
				System.out.print(", Status:" + resultSet.getString("Status"));
				System.out.print(", Comment:" + resultSet.getString("Comment"));

				InputStream inputStreamPhoto = resultSet.getBinaryStream("Photo");
				System.out.println(", Photo:" + inputStreamPhoto);

				String fileFullName = "./MyPhoto" + recId + ".png";
				boolean status = writeFileBinary(fileFullName, inputStreamPhoto);
				if (status)
					System.out.println("Write file:" + fileFullName + " okay");

				System.out.println();
			}
		}
		finally {
			if (statement!=null) {
				statement.close();
				System.out.println("connection.statement closed.");
			}
		}
		return row;
	}//select_ProductRecords

	private boolean writeFileBinary(String fileFullName, InputStream inputStream)
	{
		if (fileFullName==null || inputStream==null)
			return false;

		FileOutputStream fileOutputStream = null;
		try
		{
			fileOutputStream = new FileOutputStream(fileFullName);

			int lenRead;
			byte[] byteBuf = new byte[1000];
			while((lenRead = inputStream.read(byteBuf)) != -1) {
				fileOutputStream.write(byteBuf, 0, lenRead);
			}

			return true;
		}
		catch(Exception ex) {
			System.out.println("WriteFileError:" + ex.getMessage());
			return false;
		}
		finally
		{
			try {
				if (inputStream!=null) inputStream.close();
				if (fileOutputStream!=null) fileOutputStream.close();
			}
			catch(IOException ex) {
				System.out.println("Error:" + ex.getMessage());
			}
		}
	}

	public int selectOrderRecords(Connection connection) throws SQLException {
		System.out.println();
		System.out.println("-SelimSql selectOrderRecords operations ------");
		Statement statement = null;
		int row = 0;
		try {
			statement = connection.createStatement();
			System.out.println("connection.statement okay.");

			String sql = "select * from Order where OrderAmount>=3 and Status='A'";
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next())
			{
				row++;
				System.out.print(row + ". OrderId:" + resultSet.getInt(1));
				System.out.print(", CustomerId:" + resultSet.getInt(2));
				System.out.print(", ProductId:" + resultSet.getInt(3));
				System.out.print(", OrderDate:" + resultSet.getDate(4));
				System.out.print(", OrderAmount:" + resultSet.getShort(5));
				System.out.print(", UnitPrice:" + resultSet.getDouble(6));
				System.out.println(", PaymentType:" + resultSet.getString(7));
				System.out.println();
			}
		}
		finally {
			if (statement!=null) {
				statement.close();
				System.out.println("connection.statement closed.");
			}
		}
		return row;
	}//select_OrderRecords
}
