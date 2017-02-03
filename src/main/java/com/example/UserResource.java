package com.example;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import classes.User;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	@Produces("application/text")
	public Response createUser(String myJson) throws SQLException, IOException {

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
		DbHelper.executeInsertDb(createUserString);

		String createUserDataString = "INSERT INTO PUBLIC.USER_DATA_GENERAL (UID, START_WEIGHT, START_DATE, BIRTHDATE) "
				+ "VALUES ("
				+ maxUID
				+ ", "
				+ user.getStartWeight()
				+ ", \'"
				+ sDate + "\', \'" + bDate + "\');";
		DbHelper.executeInsertDb(createUserDataString);

		// String query =
		// "SELECT * FROM USERS u, USER_DATA_GENERAL ug WHERE UID = "
		// + maxUID + ");";
		// JSONArray json = DbHelper.executeQueryDb(query);
		// return Response.status(200).entity(json.toString()).build();
		return Response.status(200).build();

	}

	private String convertDate(Date dateToFormat) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dateToFormat);
	}

	public ResultSet retrieveSQLQueryHikari(String query) throws SQLException {
		DataSource ds = Utils.getDataSource();
		Statement stmt = ds.getConnection().createStatement();
		try {
			ResultSet result = stmt.executeQuery(query);
			return result;
		} finally {
			if (ds.getConnection() != null) {
				stmt.close();
				ds.getConnection().close();
			}
		}
	}
}