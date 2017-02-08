package classes;

public final class MyConstants {

	private static final String dbConnection = "jdbc:hsqldb:file:database/myUserDb;hsqldb.write_delay=false";
	private static final String dbUser = "SA";
	private static final String dbPassword = "sa";
	private static final String httpCode = "httpCode";
	
	public static String getDbconnection() {
		return dbConnection;
	}
	public static String getDbuser() {
		return dbUser;
	}
	public static String getDbPassword() {
		return dbPassword;
	}
	public static String getHttpCode() {
		return httpCode;
	}
	
}
