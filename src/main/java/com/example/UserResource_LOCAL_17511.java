package com.example;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import classes.MyConstants;
import classes.User;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/myUsers")
public class UserResource {
	
	final static Logger logger = Logger.getLogger(UserResource.class);

	@GET
	@Path("/full/{id}")
	@Produces("application/json")
	public Response getUserById(@PathParam("id") String id)
			throws SQLException, IOException {

		JSONArray json = DbHelper.executeQueryDb(DbHelper.getUserQueryById(id));

		return Response.status(200).header("Access-Control-Allow-Origin", "http://localhost").entity(json.toString()).build();
	}

	@GET
	@Path("/full")
	@Produces("application/json")
	public Response getAllUsers() throws SQLException, IOException {

		String query = "SELECT * FROM USERS u, USER_DATA_GENERAL ug WHERE"
				+ " (u.UID = ug.UID);";
		JSONArray json = DbHelper.executeQueryDb(query);
		
		return Response.status(200).header("Access-Control-Allow-Origin", "http://localhost").entity(json.toString()).build();
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

		return Response.status(200).header("Access-Control-Allow-Origin", "http://localhost").entity(json.toString()).build();
	}

	@POST
	@Path("/post")
	@Produces("application/json")
	public Response createUser(String myJson) throws SQLException, IOException {

		JSONArray jsonResponses = new JSONArray();
		DataSource ds = Utils.getDataSource();

		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(myJson, User.class);

		int maxUID = user.getUid();

		String sDate = checkDateForNull(user.getStartDate());

		String bDate = checkDateForNull(user.getBirthDate());

		String createUserString = "INSERT INTO PUBLIC.USERS (UID, FIRST_NAME, LAST_NAME, NICKNAME) "
				+ "VALUES ("
				+ maxUID
				+ ", \'"
				+ user.getFirstName()
				+ "\', \'"
				+ user.getLastName() + "\', \'" + user.getNickName() + "\');";

//		jsonResponses.put(DbHelper.executeInsertDbOnePhase(createUserString, ds));

		String createUserDataString = "INSERT INTO PUBLIC.USER_DATA_GENERAL (UID, START_WEIGHT, START_DATE, BIRTHDATE) "
				+ "VALUES ("
				+ maxUID
				+ ", "
				+ user.getStartWeight()
				+ ", \'"
				+ sDate + "\', \'" + bDate + "\');";
		
		String[] sqlQueries = new String[5]; 
		sqlQueries[0] = createUserString;
		sqlQueries[1] = createUserDataString;
		
//		List<String> listOfQueries = getCleanArrayList(sqlQueries);
		
		jsonResponses.put(DbHelper.executeInsertDbTwoPhase(getCleanArrayList(sqlQueries), ds));

		JSONObject jsonFinal = Helpers.getBestResponse(jsonResponses);
		
		return Response.status((int) jsonFinal.get(MyConstants.getHttpCode()))
				.header("Access-Control-Allow-Origin", "http://localhost").entity(jsonFinal.toString()).build();
	}

	public List<String> getCleanArrayList(String[] sqlQueries) {
		List<String> listOfQueries = new ArrayList<String>(Arrays.asList(sqlQueries));
		listOfQueries.removeAll(Arrays.asList("", null));
		return listOfQueries;
	}

	@DELETE
	@Path("/delete/{id}")
	@Produces("application/json")
	public Response deleteUserById(@PathParam("id") String id)
			throws SQLException, IOException {

		JSONArray jsonResponses = new JSONArray();
		DataSource ds = Utils.getDataSource();

		if (DbHelper.executeQueryDb(DbHelper.getUserQueryById(id)).isNull(0)) {
			JSONObject noUserFound = new JSONObject();
			noUserFound.put(MyConstants.getHttpCode(), 404);
			noUserFound.put("Message", "No user found for id = " + id);
			jsonResponses.put(noUserFound);
		} else {

			String query = "DELETE FROM USERS WHERE UID = " + id + ";";
			
			jsonResponses.put(DbHelper.executeDeleteDb(query, ds));

			query = "DELETE FROM PUBLIC.USER_DATA_GENERAL WHERE UID = " + id
					+ ";";
			
			jsonResponses.put(DbHelper.executeDeleteDb(query, ds));
		}

		JSONObject jsonFinal = Helpers.getBestResponse(jsonResponses);
		
		return Response.status((int) jsonFinal.get(MyConstants.getHttpCode()))
				.header("Access-Control-Allow-Origin", "http://localhost").entity(jsonFinal.toString()).build();
	}

	@PUT
	@Path("/update/{id}")
	@Produces("application/json")
	public Response updateUserById(@PathParam("id") String id, String myJson)
			throws SQLException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(myJson, User.class);

		JSONArray jsonResponses = new JSONArray();
		DataSource ds = Utils.getDataSource();

		if (DbHelper.executeQueryDb(DbHelper.getUserQueryById(id)).isNull(0)) {
			JSONObject noUserFound = new JSONObject();
			noUserFound.put(MyConstants.getHttpCode(), 404);
			noUserFound.put("Message", "No user found for id = " + id);
			jsonResponses.put(noUserFound);
		} else {

			String query = "UPDATE PUBLIC.USERS SET FIRST_NAME = \'"
					+ user.getFirstName() + "\', LAST_NAME = \'"
					+ user.getLastName() + "\', NICKNAME = \'"
					+ user.getNickName() + "\' WHERE UID = " + id + ";";

			jsonResponses.put(DbHelper.executeInsertDbOnePhase(query, ds));

			query = "UPDATE PUBLIC.USER_DATA_GENERAL SET START_WEIGHT = \'"
					+ user.getStartWeight() + "\', START_DATE = \'"
					+ convertDate(user.getStartDate()) + "\', BIRTHDATE = \'"
					+ convertDate(user.getBirthDate()) + "\' WHERE UID = " + id
					+ ";";

			jsonResponses.put(DbHelper.executeInsertDbOnePhase(query, ds));
		}

		JSONObject jsonFinal = Helpers.getBestResponse(jsonResponses);

		return Response.status((int) jsonFinal.get(MyConstants.getHttpCode()))
				.header("Access-Control-Allow-Origin", "http://localhost").entity(jsonFinal.toString()).build();
	}

	private String convertDate(Date dateToFormat) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dateToFormat);
	}

	public String checkDateForNull(Date date) {
		String convertedDate;
		if (date != null) {
			return convertedDate = convertDate(date);
		} else {
			return convertedDate = convertDate(new Date(0));
		}
	}
}
