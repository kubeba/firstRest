package com.example;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class DbHelper {
	
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

}
