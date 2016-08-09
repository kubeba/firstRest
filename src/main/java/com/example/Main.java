package com.example;

import java.io.IOException;

import javax.sql.DataSource;

import org.glassfish.grizzly.http.server.HttpServer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
//    public static final String BASE_URI = "http://localhost:8080/myapp/";
//
//    /**
//     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
//     * @return Grizzly HTTP server.
//     */
//    public static HttpServer startServer() {
//        // create a resource config that scans for JAX-RS resources and providers
//        // in com.example package
//        final ResourceConfig rc = new ResourceConfig().packages("com.example");
//
//        // create and start a new instance of grizzly http server
//        // exposing the Jersey application at BASE_URI
//        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
//    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	
    	String dbFile = "";
    	
    	HikariConfig hikariConfig = new HikariConfig();
    	hikariConfig.setAutoCommit(false);
    	hikariConfig.setConnectionTimeout(5000);
        hikariConfig.setDriverClassName(org.hsqldb.jdbc.JDBCDriver.class.getName());
        hikariConfig.setIdleTimeout(120000L);
        hikariConfig.setInitializationFailFast(true);
        hikariConfig.setJdbcUrl("jdbc:hsqldb:file:" + dbFile + ";hsqldb.write_delay=false");
        hikariConfig.setMaximumPoolSize(pConfig.getMaxPoolSize());
        hikariConfig.setPassword("sa123");
//        hikariConfig.setPoolName(“test pool");
        hikariConfig.setUsername("SA");
        
    	DataSource ds = new HikariDataSource(hikariConfig);
    	
    	

    	
//        final HttpServer server = Utils.startServer();
//        System.out.println(String.format("Jersey app started with WADL available at "
//                + "%sapplication.wadl\nHit enter to stop it...", Utils.BASE_URI));
//        System.in.read();
//        server.stop();
    }
}

