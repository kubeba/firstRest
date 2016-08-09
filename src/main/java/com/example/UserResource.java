package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/myUsers")
public class UserResource {
 
    @GET
	@Path("{id}")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") String id) {
    	
    	return Response.status(200).entity("getUserById is called, id = " + id).build();
    	
    }
}