package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
//import javax.xml.ws.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

public class MyResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Utils.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Utils.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        String responseMsg = target.path("myresource").request().get(String.class);
        assertEquals("Got it bitch!", responseMsg);
    }
    
    @Test
    public void testUserResource() {
    	String testString = "1";
        String responseMsg = target.path("myUsers/full/" + testString).request().get(String.class);
        assertThat(responseMsg, JUnitMatchers.containsString("FIRST_NAME"));
        assertThat(responseMsg, JUnitMatchers.containsString("\"UID\""));
    }
    
    @Test
    public void testUserResourceHttpCodeGetAll() {
    	Response response = target.path("myUsers/full/").request().get();
        assertEquals(response.getStatus(), 200);
    }
    
    @Test
    public void testUserResourceHttpCodeQuery() {
    	Response response = target.path("myUsers/full/query?name=e").request().get();
        assertEquals(response.getStatus(), 200);
    }
    
    @Test
    public void testSubResource() {
        String responseMsg = target.path("helloworld/subResource").request().get(String.class);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String myDate = df.format(Calendar.getInstance().getTime());
        assertTrue("Search String not found in answer", responseMsg.contains(myDate));
    }
}
