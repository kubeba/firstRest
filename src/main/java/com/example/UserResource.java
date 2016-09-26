package com.example;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Path("/myUsers")
public class UserResource {

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getUserById(@PathParam("id") String id) {
		return Response.status(200).entity("getUserById is called, id = " + id).build();
	}

	@GET
	@Produces("application/json")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Response getAllUsers() throws SQLException, IOException {

		String query = "SELECT * FROM PUBLIC.USERS";
		
		JSONArray json = new JSONArray();

		DataSource ds = Utils.getDataSource();
		Statement stmt = ds.getConnection().createStatement();
		ResultSet result = stmt.executeQuery(query);
		
		DbHelper.getReadResultSetToJsonArray(json, result);
		
		return Response.status(200).entity(json.toString()).build();
	}

}