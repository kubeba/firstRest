package com.example;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.MyConstants;
import classes.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

@Path("/myUsers")
public class UserResource {

	@GET
	@Path("/full/{id}")
	@Produces("application/json")
	public Response getUserById(@PathParam("id") String id)
			throws SQLException, IOException {

		String query = "SELECT * FROM USERS u, USER_DATA_GENERAL ug WHERE"
				+ " (u.UID = ug.UID) and ug.UID = " + id + ";";

		JSONArray json = DbHelper.executeQueryDb(query);

		return Response.status(200).entity(json.toString()).build();
	}

	@GET
	@Path("/full")
	@Produces("application/json")
	public Response getAllUsers() throws SQLException, IOException {

		String query = "SELECT * FROM USERS u, USER_DATA_GENERAL ug WHERE"
				+ " (u.UID = ug.UID);";
		JSONArray json = DbHelper.executeQueryDb(query);

		return Response.status(200).entity(json.toString()).build();
	}

	@GET
	@Path("/full/query")
	@Produces("application/json")
	public Response queryAllUsers(@QueryParam("name") String name)
			throws SQLException, IOException {

		
		String query = "SELECT * FROM USERS u, USER_DATA_GENERAL ug WHERE"
				+ " (u.UID = ug.UID) " + "AND ((LOWER(u.LAST_NAME) LIKE \'%"
				+ name.toLowerCase() + "%\') OR (LOWER(u.FIRST_NAME) LIKE \'%"
				+ name.toLowerCase() + "%\') OR (LOWER(u.NICKNAME) LIKE \'%"
				+ name.toLowerCase() + "%\'));";
		
		JSONArray json = DbHelper.executeQueryDb(query);
		
		return Response.status(200).entity(json.toString()).build();
	}

	@POST
	@Path("/post")
	@Produces("application/json")
	public Response createUser(String myJson) throws SQLException, IOException {

		JSONObject jsonResponse = new JSONObject();
		DataSource ds = Utils.getDataSource();
		
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(myJson, User.class);

		// int maxUID = maxUserId();
		int maxUID = user.getUid();

		String sDate = convertDate(user.getStartDate());
		String bDate = convertDate(user.getBirthDate());

		String createUserString = "INSERT INTO PUBLIC.USERS (UID, FIRST_NAME, LAST_NAME, NICKNAME) "
				+ "VALUES ("
				+ maxUID
				+ ", \'"
				+ user.getFirstName()
				+ "\', \'"
				+ user.getLastName() + "\', \'" + user.getNickName() + "\');";
		
		JSONObject createUserResponse = DbHelper.executeInsertDb(createUserString, ds);
		
		String createUserDataString = "INSERT INTO PUBLIC.USER_DATA_GENERAL (UID, START_WEIGHT, START_DATE, BIRTHDATE) "
				+ "VALUES ("
				+ maxUID
				+ ", "
				+ user.getStartWeight()
				+ ", \'"
				+ sDate + "\', \'" + bDate + "\');";
		JSONObject createUserDataResponse = DbHelper.executeInsertDb(createUserDataString, ds);
		
		if (createUserResponse.get(MyConstants.getHttpCode()).equals(createUserDataResponse.get(MyConstants.getHttpCode()))) {
			jsonResponse = createUserResponse;
		} else {
			if ((int) createUserResponse.getInt(MyConstants.getHttpCode()) > (int) createUserDataResponse.get(MyConstants.getHttpCode())){
				jsonResponse = createUserResponse;
				} else {
				jsonResponse = createUserDataResponse;
			}
		}
		
		return Response.status((int) jsonResponse.get(MyConstants.getHttpCode())).entity(jsonResponse.toString()).build();

	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces("application/json")
	public Response deleteUserById(@PathParam("id") String id)
			throws SQLException, IOException {

		JSONObject jsonResponse = new JSONObject();
		DataSource ds = Utils.getDataSource();
		
		String query = "DELETE FROM USERS WHERE UID = " + id + ";";

		JSONObject jsonDelUser = DbHelper.executeDeleteDb(query, ds);
		
		query = "DELETE FROM PUBLIC.USER_DATA_GENERAL WHERE UID = " + id + ";";
		
		JSONObject jsonDelUserData = DbHelper.executeDeleteDb(query, ds);
		
		if (jsonDelUser.get(MyConstants.getHttpCode()).equals(jsonDelUserData.get(MyConstants.getHttpCode()))) {
			jsonResponse = jsonDelUser;
		} else {
			if ((int) jsonDelUser.getInt(MyConstants.getHttpCode()) > (int) jsonDelUserData.get(MyConstants.getHttpCode())){
				jsonResponse = jsonDelUser;
				} else {
				jsonResponse = jsonDelUserData;
			}
		}

		return Response.status((int) jsonResponse.get(MyConstants.getHttpCode())).entity(jsonResponse.toString()).build();
	}

	private String convertDate(Date dateToFormat) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dateToFormat);
	}
}

