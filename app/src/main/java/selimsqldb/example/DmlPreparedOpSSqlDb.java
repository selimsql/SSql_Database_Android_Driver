package selimsqldb.example;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DmlPreparedOpSSqlDb {

	private int preparedStatementExecute(PreparedStatement preparedStatement, Object[] colValues) {
		try {
			int lenCol = (colValues==null ? 0 : colValues.length);
			for(int i=0; i < lenCol; i++) {
				preparedStatement.setObject(i + 1, colValues[i]);
			}

			int number = preparedStatement.executeUpdate();
			System.out.println("-PreparedStatement executed values");
			System.out.println();

			return number;
		}
		catch(SQLException ex) {
			System.out.println("Error:" + ex.getMessage());
			return 0;
		}
	}

	private byte[] readFileBinaryAsBytes(String fileFullName)
	{
		FileInputStream fileInputStream = null;
		ByteArrayOutputStream baos = null;
		try
		{
			fileInputStream = new FileInputStream(fileFullName);
			baos = new ByteArrayOutputStream();

			int sizeTemp;
			byte[] bytesTemp = new byte[1000];
			while((sizeTemp = fileInputStream.read(bytesTemp)) != -1) {
				baos.write(bytesTemp, 0, sizeTemp);
			}

			return baos.toByteArray();
		}
		catch(Exception ex) {
			System.out.println("ReadFileError:" + ex.getMessage());
			return null;
		}
		finally
		{
			try {
				if (fileInputStream!=null) fileInputStream.close();
				if (baos!=null) baos.close();
			}
			catch(IOException ex) {
				System.out.println("Error:" + ex.getMessage());
			}
		}
	}

	public int insertUpdateRecords(Connection connection) throws SQLException {
		System.out.println("-SelimSql insertUpdate operations ------");
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {
			connection.setAutoCommit(false);
			System.out.println("connection.AutoCommit:false");

			String sql = "Insert into Product(Id, Name, Price, ProductDate, Status, Comment, Photo) VALUES(?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			//statement.setMaxRows(100);
			System.out.println("connection.preparedStatement(sql:" + sql + ") okay.");
			System.out.println();

			java.util.Date dateToday = new java.util.Date();
			Object[] recArray = new Object[]{new Object[]{new Integer(11), "Product11", new Double(22.35), dateToday, "A", null, null},
											 new Object[]{new Integer(12), "Product12", new Double(32.68), dateToday, "P", null, null},
											 new Object[]{new Integer(13), "Product13", new Double(97), dateToday, "A", null, null},
											};

			//Insert Product rows ---------------
			int countArray = recArray.length;
			for(short i=0; i < countArray; i++) {
				Object[] colValues = (Object[])recArray[i];
				count += preparedStatementExecute(preparedStatement, colValues);
			}

			connection.commit();
			System.out.println("connection.commit okay for inserts");

			//------------------
			sql = "Update Product set Comment = ? where Id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "Update Comment Long Description..");

			//Example for BLOB data update:
			//sql = "Update Product set Comment = ?, Photo = ? where Id = ?";
			//byte[] imageBytes = readFileBinaryAsBytes("./Notebook.png");
			//preparedStatement.setBytes(2, imageBytes);

			preparedStatement.setInt(2, 13);

			int number = preparedStatement.executeUpdate();
			System.out.println("-PreparedStatement executed for update. Affected row:" + number);
			System.out.println();

			//------------------
			connection.commit();
			System.out.println("connection.commit okay for update");
		}
		finally {
			if (preparedStatement!=null) {
				preparedStatement.close();
				System.out.println("connection.preparedStatement closed.");
			}
		}

		return count;
	}
}
