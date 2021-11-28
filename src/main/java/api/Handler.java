package api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import org.postgis.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class Handler {

	static final String DB_URL = "jdbc:postgresql://localhost:25432/SixFtTracing"; // Change SixFtTracing to name of database
    static final String USER = "docker";
    static final String PASS = "docker";

	@PostMapping("/upload")
	public String dumpData(@RequestBody String data) throws Exception {
		JSONArray obj = new JSONArray(data);
		
		JSONObject returnObj = new JSONObject();
		try
		{
			//WRITE CODE HERE
			/*
			 * Sample value in "obj" is as follows (array of JSONObjects)
			 *[
			 *  {
			 *     "latitude":13.23232,
			 *     "longitude":-112.32323,
			 *     "sourceDevice":"arun",
			 *     "destinationDevice":"mani",
			 *     "timeStamp":12323242324
			 *  },
			 *  {
			 *     "latitude":14.22432,
			 *     "longitude":-117.837233,
			 *     "sourceDevice":"rucha",
			 *     "destinationDevice":"baslyos",
			 *     "timeStamp":12323242324
			 *  }
			 * ]
			 */

			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//((org.postgresql.PGConnection)conn).addDataType("geometry", PGgeometry.class);
			//((org.postgresql.PGConnection)conn).addDataType("geography", PGgeography.class);
			((org.postgresql.PGConnection)conn).addDataType("box3d", PGbox3d.class);
			Statement stmt = conn.createStatement();
		
			for(int i = 0; i < obj.length(); i++) {
				JSONObject entry = obj.getJSONObject(i);
				long contact_time = (Long) entry.get("timeStamp");
				String user_id_one = entry.get("sourceDevice").toString();
				String user_id_two = entry.get("destinationDevice").toString();
				double latitude = (Double) entry.get("latitude");
				double longitude = (Double) entry.get("longitude");


				String QUERY = "INSERT INTO \"ContactTracingMaster\"( \n" +
                    "\tcontact_time, user_id_one, user_id_two, location_of_contact) \n" +
                    "\tVALUES(TO_TIMESTAMP(" + contact_time + "), " + user_id_one + ", " + user_id_two + ", " + "POINT(" + longitude + " " + latitude + ")";
				stmt.executeUpdate(QUERY);
			}
			
			returnObj.put("message", "success");
			returnObj.put("data", obj.toString()); //Currently included for testing
			returnObj.put("status_code", 200);
		}catch(Exception e)
		{
			returnObj = new JSONObject();
			returnObj.put("message", "internal server error");
			returnObj.put("status_code", 500);
		}
		return returnObj.toString();
		
	}
}
