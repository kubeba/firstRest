package com.example;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.MyConstants;

public class DbHelper {
	
	public static JSONObject executeInsertDb(String sqlQuery, DataSource ds) throws SQLException {    
		JSONObject json = new JSONObject();
    	Connection con = ds.getConnection();
    	Statement stmt = con.createStatement();
    	
	    try {
			stmt.executeQuery(sqlQuery);
			json.put("Message", "Everything's fine.");
			json.put("httpCode", 200);		
			stmt.execute("COMMIT;");
			return json;
	    } catch (SQLException e) {
	    	json.put("errorCode", e.getErrorCode());
	    	json.put("errorMessage", e.getMessage());
	    	json.put("sqlState", e.getSQLState());
	    	json.put("httpCode", 500);
	    	stmt.execute("ROLLBACK;");
	    	return json;
	    } finally {
			con.close();
			stmt.close();			
		}
	}
	
	public static JSONArray executeQueryDb(String sqlQuery) throws SQLException {   
		
		JSONArray json = new JSONArray();
		DataSource ds = Utils.getDataSource();
    	Connection con = ds.getConnection();
    	Statement stmt = con.createStatement();
    	
	    try {
	    	ResultSet rs = stmt.executeQuery(sqlQuery);
	    	getReadResultSetToJsonArray(json, rs);
	        return json;               
	    } catch (SQLException e) {
	    	JSONObject jso = new JSONObject();
	    	jso.put("errorCode", e.getErrorCode());
	    	jso.put("errorMessage", e.getMessage());
	    	jso.put("sqlState", e.getSQLState());
	    	jso.put("httpCode", 500);
	    	json.put(jso);
	        return json;
	    } finally {
	    	stmt.close();
	    	con.close();
	    }
	}
	
	public static JSONObject executeDeleteDb(String sqlQuery, DataSource ds) throws SQLException {    
		JSONObject json = new JSONObject();
    	Connection con = ds.getConnection();
    	Statement stmt = con.createStatement();
    	
	    try {
			stmt.executeQuery(sqlQuery);
			json.put("Message", "Everything's fine. Contact deleted." );
			json.put("httpCode", 200);		
			stmt.execute("COMMIT;");
			return json;
	    } catch (SQLException e) {
	    	json.put("errorCode", e.getErrorCode());
	    	json.put("errorMessage", e.getMessage());
	    	json.put("sqlState", e.getSQLState());
	    	json.put("httpCode", 500);
	    	stmt.execute("ROLLBACK;");
	    	return json;
	    } finally {
			con.close();
			stmt.close();			
		}
	}
	
	protected static JSONArray getReadResultSetToJsonArray(JSONArray json, ResultSet result) throws SQLException {
		ResultSetMetaData md = result.getMetaData();
		int columns = md.getColumnCount();
		
		while (result.next()) {
			
			JSONObject obj = new JSONObject();
			
			for (int i = 1; i <= columns; i++) {
				
				String columnName = md.getColumnName(i);
				
				if (md.getColumnType(i) == java.sql.Types.ARRAY) {
					obj.put(columnName, result.getArray(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.BIGINT) {
					obj.put(columnName, result.getInt(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.BOOLEAN) {
					obj.put(columnName, result.getBoolean(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.BLOB) {
					obj.put(columnName, result.getBlob(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.DOUBLE) {
					obj.put(columnName, result.getDouble(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.FLOAT) {
					obj.put(columnName, result.getFloat(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.INTEGER) {
					obj.put(columnName, result.getInt(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.NVARCHAR) {
					obj.put(columnName, result.getNString(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.VARCHAR) {
					obj.put(columnName, result.getString(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.TINYINT) {
					obj.put(columnName, result.getInt(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.SMALLINT) {
					obj.put(columnName, result.getInt(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.DATE) {
					obj.put(columnName, result.getDate(columnName));
				} else if (md.getColumnType(i) == java.sql.Types.TIMESTAMP) {
					obj.put(columnName, result.getTimestamp(columnName));
				} else {
					obj.put(columnName, result.getObject(columnName));
				}
			}
			json.put(obj);
		}
		return json;
	}

	public static String getUserQueryById(String id){
		String query = "SELECT * FROM USERS u, USER_DATA_GENERAL ug WHERE"
				+ " (u.UID = ug.UID) and ug.UID = " + id + ";";
		return query;
	}
}
