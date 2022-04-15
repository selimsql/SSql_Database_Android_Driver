package selimsqldb.example;

public class DbConstant {
	public static final String SELIMSQL_JDBC_CLASS      = "selimsql.jdbc.DbDriver";
	public static final String SELIMSQL_JDBC_URL_HEADER = "jdbc:selimsql:";

	public static final String SELIMSQL_DB_TYPE_FILENEWDATABASE  = "fileNewDatabase";
	//public static final String SELIMSQL_DB_TYPE_FILEDROPDATABASE = "fileDropDatabase";

	public static final String SELIMSQL_DB_TYPE_FILE   = "file"; //Usage example: "jdbc:selimsql:file:MyDb"
	//public static final String SELIMSQL_DB_TYPE_HOST = "//";   //Usage example: "jdbc:selimsql://192.168.2.3:9933/MyDb"

	public static final String SELIMSQL_DB_TEST_NAME   = "MyDb";
	public static final String USER_ADMIN              = "admin";
}
