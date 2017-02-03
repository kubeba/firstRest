package com.example;

import java.net.URI;
import java.util.Random;

import javax.sql.DataSource;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Utils {
	
	public static final String BASE_URI = "http://localhost:8080/myapp/";
		
    public static HttpServer startServer() {

    	final ResourceConfig rc = new ResourceConfig().packages("com.example");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
    
    public static String randomString(final int length) {
    	
    	char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    	StringBuilder sb = new StringBuilder();
    	Random random = new Random();
    	for (int i = 0; i < length; i++) {
    	    char c = chars[random.nextInt(chars.length)];
    	    sb.append(c);
    	}
        return sb.toString();
    }
    
    public static DataSource getDataSource() {
    	
    	String dbFile = "/Users/Shared/hsqldb/databases/myUserDb";
    	
    	HikariConfig hikariConfig = new HikariConfig();
    	hikariConfig.setAutoCommit(false);
    	hikariConfig.setConnectionTimeout(5000);
        hikariConfig.setDriverClassName(org.hsqldb.jdbc.JDBCDriver.class.getName());
        hikariConfig.setIdleTimeout(120000L);
        hikariConfig.setInitializationFailFast(true);
        hikariConfig.setJdbcUrl("jdbc:hsqldb:file:" + dbFile + ";hsqldb.write_delay=false");
        hikariConfig.setMaximumPoolSize(1);
        hikariConfig.setPassword("sa");
        hikariConfig.setUsername("SA");
        
    	DataSource ds = new HikariDataSource(hikariConfig);
    	
		return ds;
    	
    }

}
