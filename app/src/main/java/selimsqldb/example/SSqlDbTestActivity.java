package selimsqldb.example;

import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;

public class SSqlDbTestActivity extends AppCompatActivity {

	private static final String appStorageRootDir_KEY = "appStorageRootDir";
	private static final int REQUEST_PERMISSION = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlayout);

		updateAppDbStorageRootDir();

		if (storagePermissionGranted()) {
			showToast("Storage permissions are ALREADY GRANTED");
		}
		else {
			requestPermissionsAfterApi23ByHandler();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	//Application install database root directory on android device.
	private void updateAppDbStorageRootDir() {
		int androidApiVersion = android.os.Build.VERSION.SDK_INT;
		String appStorageRootDir;
		//if Api30+(Android11+)
		if (androidApiVersion >= 30) {
			java.io.File fileRootDir = getExternalFilesDir(null);
			//ex: "/storage/emulated/0/Android/data/selimsqldb.example/files"
			appStorageRootDir = fileRootDir.getPath();
		}
		else {
			java.io.File fileRootDir = Environment.getExternalStorageDirectory();
			//ex: "/storage/emulated/0"
			appStorageRootDir = fileRootDir.getPath() + "/MyAppDir";
			//ex: "/storage/emulated/0/MyAppDir"
		}

		//Necessary for set root directory of SelimSql database!
		System.setProperty(appStorageRootDir_KEY, appStorageRootDir);
	}


	//Application needs read/write permissions for database operations on android device.
	private boolean storagePermissionGranted() {
		if (android.os.Build.VERSION.SDK_INT < 23) {
			return true;
		}

		//Check after Api23+
		int granted = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		return (granted == PackageManager.PERMISSION_GRANTED);
	}

	//Application requires read/write permissions from android device user.
	private void requestPermissionsAfterApi23ByHandler() {
		showToast("requestPermissions for storage");

		Runnable task = new Runnable() {
			@Override
			public void run() {
				requestPermissionsAfterApi23();
			}
		};

		Handler handler = new Handler();
		handler.postDelayed(task, 2000); //After 2 secs.
	}

	@RequiresApi(api = 23)
	private boolean requestPermissionsAfterApi23() {
		//Api23+
		final String[] permissions = new String[] {
				android.Manifest.permission.READ_EXTERNAL_STORAGE,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

		this.requestPermissions(permissions, REQUEST_PERMISSION);
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode != REQUEST_PERMISSION) {
			return;
		}

		int grantCount = (grantResults == null ? 0 : grantResults.length);
		for(int i = 0; i < grantCount; i++) {
			if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
				finish(); //Force onStop the app!
				return;
			}
		}

		showToast("requestPermissions for storage GRANTED");
	}


	private void showToast(String info) {
		Toast toast = Toast.makeText(this, info, Toast.LENGTH_LONG);
		toast.show();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int itemId = item.getItemId();
        if (itemId == R.id.menu_new_database) {
        	return doNewDatabase();
        }
        else
        if (itemId == R.id.menu_connect_database) {
			return doConnectDatabase();
        }
        else
        if (itemId == R.id.menu_build_tables) {
			return doBuildTables();
        }
        else
        if (itemId == R.id.menu_insert_update_records) {
			return doInsertUpdateRecords();
        }
        else
        if (itemId == R.id.menu_insert_update_by_prepared) {
			return doInsertUpdateRecordsByPrepared();
        }
        else
        if (itemId == R.id.menu_select_records) {
			return doSelectRecords();
        }

        return super.onOptionsItemSelected(item);
    }

	private String getAppStorageRootDir() {
		return appStorageRootDir_KEY + ": " + System.getProperty(appStorageRootDir_KEY);
	}

    //warning! Caused by: java.lang.RuntimeException: java.io.FileNotFoundException: /storage/emulated/0/SelimSql/confSSql/selimSql.ini (Permission denied)
	private boolean doNewDatabase() {
		Connection connection = null;
		NewSSqlDb newSSqlDb = new NewSSqlDb();
		String msg;
		try {
			//ex: "jdbc:selimsql:fileNewDatabase:MyDb" url used to build a new database on AppRootDir.
			connection = newSSqlDb.newDb(DbConstant.SELIMSQL_DB_TEST_NAME);
			//...
			msg = "NewDb: " + DbConstant.SELIMSQL_DB_TEST_NAME + " BUILT"
				+ "\n" + getAppStorageRootDir();
		}
		catch(SQLException ex) {
			msg = "SQLException:" + ex.getMessage();
			System.out.println(msg);
		}
		finally {
			newSSqlDb.closeDb(connection);
		}

		showToast(msg);
		return (connection != null);
    }


    private boolean doConnectDatabase() {
		Connection connection = null;
		ConnectSSqlDb connectSSqlDb = new ConnectSSqlDb();
		try {
			//ex: "jdbc:selimsql:file:MyDb" url used to connect an existing database on AppRootDir.
			connection = connectSSqlDb.connectDb(DbConstant.SELIMSQL_DB_TEST_NAME);

			//...
		}
		finally {
			connectSSqlDb.closeDb(connection);
		}

		String msg = "ConnectDb: " + DbConstant.SELIMSQL_DB_TEST_NAME + " " + (connection == null ? "FAILED" : "SUCCESSFUL")
				   + "\n" + getAppStorageRootDir();
		showToast(msg);
		return (connection != null);
    }

    private boolean doBuildTables() {
		Connection connection = null;
		String error = null;
		ConnectSSqlDb connectSSqlDb = new ConnectSSqlDb();
		try {
			connection = connectSSqlDb.connectDb(DbConstant.SELIMSQL_DB_TEST_NAME);
			if (connection == null)
				throw new SQLException("Connection Failed!");

			//ex: Build Product, Customer, Order tables on database.
			BuildTableSSqlDb buildTableSSqlDb = new BuildTableSSqlDb();
			buildTableSSqlDb.buildTablesAndIndexes(connection);
		}
		catch(Exception ex) {
			error = "Error:" + ex.getMessage();
			System.out.println(error);
		}
		finally {
			connectSSqlDb.closeDb(connection);
		}

		boolean successful = (error == null);
		String msg = "BuildTables: " + (successful ? "SUCCESSFUL" : error);
		showToast(msg);
		return successful;
    }

    private boolean doInsertUpdateRecords() {
		Connection connection = null;
		String error = null;
		String msg = null;
		ConnectSSqlDb connectSSqlDb = new ConnectSSqlDb();
		try {
			connection = connectSSqlDb.connectDb(DbConstant.SELIMSQL_DB_TEST_NAME);
			if (connection == null)
				throw new SQLException("Connection Failed!");

			DmlOpSSqlDb dmlOpSSqlDb = new DmlOpSSqlDb();
			//ex: Insert table records on Product, Customer, Order tables.
			msg = "InsertRecords:" + dmlOpSSqlDb.insertUpdateDeleteRecords(connection);
		}
		catch(Exception ex) {
			error = "Error:" + ex.getMessage();
			System.out.println(error);
		}
		finally {
			connectSSqlDb.closeDb(connection);
		}

		boolean successful = (error == null);
		msg = (successful ? "SUCCESSFUL(" + msg + ")" : error);
		showToast(msg);
		return successful;
    }

    private boolean doInsertUpdateRecordsByPrepared() {
		Connection connection = null;
		String error = null;
		String msg = null;
		ConnectSSqlDb connectSSqlDb = new ConnectSSqlDb();
		try {
			connection = connectSSqlDb.connectDb(DbConstant.SELIMSQL_DB_TEST_NAME);
			if (connection == null)
				throw new SQLException("Connection Failed!");

			DmlPreparedOpSSqlDb dmlPreparedOpSSqlDb = new DmlPreparedOpSSqlDb();
			msg = "InsertRecordsByPrepared:" + dmlPreparedOpSSqlDb.insertUpdateRecords(connection);
		}
		catch(Exception ex) {
			error = "Error:" + ex.getMessage();
			System.out.println(error);
		}
		finally {
			connectSSqlDb.closeDb(connection);
		}

		boolean successful = (error == null);
		msg = (successful ? "SUCCESSFUL(" + msg + ")" : error);
		showToast(msg);
		return successful;
    }

    private boolean doSelectRecords() {
		Connection connection = null;
		String error = null;
		String msg = null;
		ConnectSSqlDb connectSSqlDb = new ConnectSSqlDb();
		try {
			connection = connectSSqlDb.connectDb(DbConstant.SELIMSQL_DB_TEST_NAME);
			if (connection == null)
				throw new SQLException("Connection Failed!");

			SelectRecSSqlDb selectRecSSqlDb = new SelectRecSSqlDb();
			int row = selectRecSSqlDb.selectProductRecords(connection);
			msg = "SelectProductRecords:" + row;

			selectRecSSqlDb.selectOrderRecords(connection);
			row = selectRecSSqlDb.selectOrderRecords(connection);
			msg += "\nSelectOrderRecords:" + row;
		}
		catch(Exception ex) {
			error = "Error:" + ex.getMessage();
			System.out.println(error);
		}
		finally {
			connectSSqlDb.closeDb(connection);
		}

		boolean successful = (error == null);
		msg = (successful ? "SUCCESSFUL(" + msg + ")" : error);
		showToast(msg);
		return successful;
    }
}
