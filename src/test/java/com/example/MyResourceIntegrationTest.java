//package com.example;
//
//import static org.junit.Assert.*;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.validation.constraints.AssertFalse;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.Response;
////import javax.xml.ws.Response;
//
//
//
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.matchers.JUnitMatchers;
//
//import classes.RestResponse;
//import classes.User;
//import static io.restassured.RestAssured.given;;
//
//public class MyResourceIntegrationTest {
//
//    private HttpServer server;
//    private WebTarget target;
//
//    @Before
//    public void setUp() throws Exception {
//        // start the server
//        server = Utils.startServer();
//        // create the client
//        Client c = ClientBuilder.newClient();
////
//        target = c.target(Utils.BASE_URI);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        server.stop();
//    }
//
//    /**
//     * Test to see that the message "Got it!" is sent in the response.
//     */
//    
//    @Test
//    public void getUser() {
//    	
//    	given().when().get("/myapp/myUsers/full/1").then().statusCode(200);
//    	
//    }
//    
//    @Test
//    public void userE2E(){
//    	
//    	String uid="1010101010";
////    	int random = (int )(Math.random() * 1000 + 1);
//    	
////    	User user = new User();
////    	user.setUid(random);
////    	user.setFirstName("fName");
////    	user.setLastName("lName");
////    	user.setNickName("nName");
////    	user.setStartWeight(100);
////    	user.setBirthDate("1999-10-23");
////    	user.setStartDate(startDate);
//    	
//    	
////    	uid = (String) random;
//    	
//    	Map<String, String> user = new HashMap<>();
//    	user.put("uid", uid);
//    	user.put("firstName", "Heinrich");
//    	user.put("lastName", "Klatze-Kleist");
//    	user.put("nickName", "Dreist");
//    	user.put("startWeight", "87");
//    	
//    	given().contentType("application/json").body(user).when().post("/myapp/myUsers/post").then().statusCode(200);
//    	given().when().get("/myapp/myUsers/full/" + uid).then().statusCode(200);
////    	given().when().post("/myapp/myUsers/delete/" + uid).then().statusCode(200);
//    }
//    
//}
