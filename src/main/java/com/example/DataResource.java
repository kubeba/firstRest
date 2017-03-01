package com.example;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

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

import classes.Data;
import classes.MyConstants;
import classes.User;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/data")
public class DataResource {

	final static Logger logger = Logger.getLogger(DataResource.class);

	@GET
	@Path("/get/{id}")
	@Produces("application/json")
	public Response getDataById(@PathParam("id") String id)
			throws SQLException, IOException {

		JSONArray json = DbHelper.executeQueryDb(DbHelper
				.getAllUserProgressById(id));

		return Response.status(200).entity(json.toString()).build();
	}

	@POST
	@Path("/post")
	@Produces("application/json")
	public Response createDataEntry(String myJson) throws SQLException,
			IOException {

		JSONArray jsonResponses = new JSONArray();
		DataSource ds = Utils.getDataSource();

		ObjectMapper mapper = new ObjectMapper();
		Data data = mapper.readValue(myJson, Data.class);

		String sDate = checkDateForNull(data.getDate());

		String createNewDate = "INSERT INTO USER_DATA_PROGRESS "
				+ "(UID, DATE, WEIGHT, FOOD, SWEETS, SPORT, KCAL_SPORT) "
				+ "VALUES ( \'" + data.getUid() + "\', \'" + sDate + "\', "
				+ data.getWeight() + ", \'" + data.getFood() + "\', "
				+ data.isSport() + ", " + data.isSweets() + ", "
				+ data.getKcal_sport() + ");";

		jsonResponses.put(DbHelper.executeInsertDb(createNewDate, ds));

		JSONObject jsonFinal = Helpers.getBestResponse(jsonResponses);

		return Response.status((int) jsonFinal.get(MyConstants.getHttpCode()))
				.entity(jsonFinal.toString()).build();
	}

	@POST
	@Path("/delete")
	@Produces("application/json")
	public Response deleteUserById(String delReqJson) throws SQLException,
			IOException {

		JSONArray jsonResponses = new JSONArray();
		DataSource ds = Utils.getDataSource();

		ObjectMapper mapper = new ObjectMapper();
		Data data = mapper.readValue(delReqJson, Data.class);

		if (DbHelper.executeQueryDb(
				DbHelper.getSingleUserProgressById(data.getUid(),
						convertDate(data.getDate()))).isNull(0)) {
			JSONObject noDataFound = new JSONObject();
			noDataFound.put(MyConstants.getHttpCode(), 404);
			noDataFound.put("Message",
					"No data entry found for id = " + data.getUid());
			jsonResponses.put(noDataFound);
		} else {
			String query = "DELETE FROM USER_DATA_PROGRESS WHERE UID = \'"
					+ data.getUid() + "\' AND DATE = \'"
					+ checkDateForNull(data.getDate()) + "\';";
			jsonResponses.put(DbHelper.executeDeleteDb(query, ds));
		}

		JSONObject jsonFinal = Helpers.getBestResponse(jsonResponses);

		return Response.status((int) jsonFinal.get(MyConstants.getHttpCode()))
				.entity(jsonFinal.toString()).build();
	}

	@PUT
	@Path("/update")
	@Produces("application/json")
	public Response updateUserById(String myJson) throws SQLException,
			IOException {

		ObjectMapper mapper = new ObjectMapper();
		Data data = mapper.readValue(myJson, Data.class);

		JSONArray jsonResponses = new JSONArray();
		DataSource ds = Utils.getDataSource();
		
		JSONArray dataFromDb = DbHelper.executeQueryDb(DbHelper.getSingleUserProgressById(data.getUid(),
				convertDate(data.getDate())));

		if (dataFromDb.isNull(0)) {
			JSONObject noDataFound = new JSONObject();
			noDataFound.put(MyConstants.getHttpCode(), 404);
			noDataFound.put("Message", "No data found for id = " + data.getUid());
			jsonResponses.put(noDataFound);
		} else {
			
			JSONObject jsonIn = new JSONObject(myJson);
			JSONObject jsonDb = new JSONObject(dataFromDb.get(0).toString());
			
			logger.info("jsonDb before: " + jsonDb);
			
			jsonDb = mergeForUpdate(jsonIn, copyToLowerKey(jsonDb));
			
			logger.info("jsonDb after: " + jsonDb);
			
			ObjectMapper mapDataFromDb = new ObjectMapper();
			Data dataDb = mapDataFromDb.readValue(jsonDb.toString(), Data.class);

			String query = "UPDATE USER_DATA_PROGRESS SET DATE = \'"
					+ checkDateForNull(dataDb.getDate()) + "\', WEIGHT = "
					+ dataDb.getWeight() + ", FOOD = \'"
					+ dataDb.getFood() + "\', SWEETS = " 
					+ dataDb.isSweets() + ", SPORT = " 
					+ dataDb.isSport() + ", KCAL_SPORT = " 
					+ dataDb.getKcal_sport() + " WHERE UID = \'" 
					+ dataDb.getUid() + "\' AND DATE = \'" 
					+ checkDateForNull(dataDb.getDate()) + "\';";

			jsonResponses.put(DbHelper.executeInsertDb(query, ds));

		}
		JSONObject jsonFinal = Helpers.getBestResponse(jsonResponses);

		return Response.status((int) jsonFinal.get(MyConstants.getHttpCode()))
				.entity(jsonFinal.toString()).build();
	}

	public JSONObject mergeForUpdate(JSONObject jsonIn, JSONObject jsonDb) {
		Iterator<?> keys = jsonIn.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (jsonDb.has(key) && !jsonIn.get(key).equals(jsonDb.get(key)) ) 
				{
				logger.info("Keys to be merged - jsonIn: " + key + " value: " +  jsonIn.get(key));
				logger.info("Keys to be merged - jsonDb: " + key  + " value: " +  jsonDb.get(key));
				jsonDb.remove(key);
				jsonDb.put(key, jsonIn.get(key));
			}
		}
		return jsonDb;
	}

	private JSONObject copyToLowerKey(JSONObject json) {

		JSONObject jsonLowerKey = new JSONObject();

		Iterator<?> keys = json.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String keyLower = key.toLowerCase();
			jsonLowerKey.put(keyLower, json.get(key));
		}
		return jsonLowerKey;
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
