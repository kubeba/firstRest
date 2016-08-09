package com.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.glassfish.grizzly.http.util.TimeStamp;
 
@Path("helloworld")
public class ClichedAnswer {
    public static final String CLICHED_MESSAGE = "OMG, it's \"Hello WORLD!\" again ... :-(";
 
@GET
@Produces("text/plain")
    public String getHello() {
        return CLICHED_MESSAGE;
    }

@GET @Path("/subResource")
@Produces("application/json")
    public String getTime() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date now = Calendar.getInstance().getTime();
		String myDate = df.format(now);
        return myDate;
    }
}
