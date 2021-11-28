package api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Throwables;

import java.sql.*;
import org.postgis.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class Handler {

	static final String DB_URL = "jdbc:postgresql://localhost:5432/SixFtTracing"; // Change SixFtTracing to name of database
    static final String USER = "postgres";
    static final String PASS = "root";

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
				String contact_time = entry.get("timeStamp").toString();
				String user_one = entry.get("sourceDevice").toString();
				String user_two = entry.get("destinationDevice").toString();
				String latitude = entry.get("latitude").toString();
				String longitude = entry.get("longitude").toString();


				String ID_QUERY1 = "SELECT id FROM \"UserMaster\" WHERE user_name = \'" + user_one + "\';";
				System.out.println(ID_QUERY1);
            	ResultSet id_one = stmt.executeQuery(ID_QUERY1);

            	int user_id_one = 0;

            	while(id_one.next()) {
                	user_id_one = Integer.parseInt(id_one.getString(1));
            	}

            	String ID_QUERY2 = "SELECT id FROM \"UserMaster\" WHERE user_name = \'" + user_two + "\';";
            	System.out.println(ID_QUERY2);
            	ResultSet id_two = stmt.executeQuery(ID_QUERY2);

            	
           		int user_id_two = 0;

            	while(id_two.next()) {
                	user_id_two = Integer.parseInt(id_two.getString(1));
            	}

            	String INSERT = "INSERT INTO \"ContactTracingMaster\"( \n" +
					"\tcontact_time, user_id_one, user_id_two, location_of_contact) \n" +
					"\tVALUES(TO_TIMESTAMP(" + contact_time + "), " + user_id_one + ", " + user_id_two + ", " + "\'POINT(" + longitude + " " + latitude + ")\');";
            	
            	System.out.println(INSERT);
				stmt.executeUpdate(INSERT);
			}

			conn.close();
			
			returnObj.put("message", "success");
			returnObj.put("data", obj.toString()); //Currently included for testing
			returnObj.put("status_code", 200);
		}catch(Exception e)
		{
			System.out.println(Throwables.getStackTraceAsString(e));
			returnObj = new JSONObject();
			returnObj.put("message", "internal server error");
			returnObj.put("status_code", 500);
		}
		return returnObj.toString();
		
	}
	
	/*
	 * User APIs
	 */
	@PostMapping("/user")
	public String createUser(@RequestBody String data) throws Exception {
		JSONArray obj = new JSONArray(data);
		
		JSONObject returnObj = new JSONObject();
		try
		{
			//WRITE CODE HERE
			/*
			 *  {
			 *     "name":"arun",
			 *     "user_name":"arunvenkatesh",
			 *     "secret_key":"password",
			 *     "registration_token":"24Zdsjbsdjadjadn283293",
			 *     "phone_number":"9512231444",
			 *     "gender":"M",
			 *     "age":"25",
			 *     "state":"California",
			 *     "city":"Riverside",
			 *     "status":-1 //-1 : Negative, 0 - Symptomatic, 1 - Positive 
			 *  }
			 */

			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//((org.postgresql.PGConnection)conn).addDataType("geometry", PGgeometry.class);
			//((org.postgresql.PGConnection)conn).addDataType("geography", PGgeography.class);
			((org.postgresql.PGConnection)conn).addDataType("box3d", PGbox3d.class);
			Statement stmt = conn.createStatement();
		
			for(int i = 0; i < obj.length(); i++) {
				JSONObject entry = obj.getJSONObject(i);
				String name = entry.get("name").toString();
				name = (name.equals("NULL")) ? name : "\'" + name + "\'";
				String user_name = entry.get("user_name").toString(); // NOT NULL value
				user_name = (user_name.equals("NULL")) ? user_name : "\'" + user_name + "\'";
				String secret_key = entry.get("secret_key").toString();
				secret_key = (secret_key.equals("NULL")) ? secret_key : "\'" + secret_key + "\'";
				String registration_token = entry.get("registration_token").toString();
				registration_token = (registration_token.equals("NULL")) ? registration_token : "\'" + registration_token + "\'";
				String phone_number = entry.get("phone_number").toString();
				phone_number = (phone_number.equals("NULL")) ? phone_number : "\'" + phone_number + "\'";
				String gender = entry.get("gender").toString();
				gender = (gender.equals("NULL")) ? gender : "\'" + gender + "\'";
				String age = entry.get("age").toString();
				String state = entry.get("state").toString();
				state = (state.equals("NULL")) ? state : "\'" + state + "\'";
				String city = entry.get("city").toString();
				city = (city.equals("NULL")) ? city : "\'" + city + "\'";
				String status = entry.get("status").toString();
				status = (status.equals("NULL")) ? status : "\'" + status + "\'";
				

				String INSERT = "INSERT INTO \"UserMaster\"( \n" +
					"\nname, user_name, secret_key, registration_token, phone_number, gender, age, state, city, status) \n" +
					"\tVALUES(" + name + ", " + user_name + ", " + secret_key + ", " + registration_token + ", " + phone_number + ", " + gender + ", " + age + ", " + state + ", " + city + ", " + status + ");";
				System.out.println(INSERT);

				stmt.executeUpdate(INSERT);
			}

			conn.close();
			
			returnObj.put("message", "success");
			returnObj.put("data", obj.toString()); //Currently included for testing
			returnObj.put("status_code", 200);
		}catch(Exception e)
		{
			System.out.println(Throwables.getStackTraceAsString(e));
			returnObj = new JSONObject();
			returnObj.put("message", "internal server error");
			returnObj.put("status_code", 500);
		}
		return returnObj.toString();
		
	}
	
	@GetMapping("/user/login")
	public String validateUser(String user_name, String password) throws Exception {
		JSONObject returnObj = new JSONObject();
		try
		{
			Boolean success = Boolean.FALSE;
			//WRITE CODE HERE

			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//((org.postgresql.PGConnection)conn).addDataType("geometry", PGgeometry.class);
			//((org.postgresql.PGConnection)conn).addDataType("geography", PGgeography.class);
			((org.postgresql.PGConnection)conn).addDataType("box3d", PGbox3d.class);
			Statement stmt = conn.createStatement();

			String USER_QUERY = "SELECT * FROM \"UserMaster\" WHERE user_name = \'" + user_name + "\';";
			System.out.println(USER_QUERY);
			ResultSet user = stmt.executeQuery(USER_QUERY);
			
			if(user.next()) {
				String PWD_QUERY = "SELECT secret_key FROM \"UserMaster\" WHERE user_name = \'" + user_name + "\';";
				System.out.println(PWD_QUERY);
				ResultSet pwd = stmt.executeQuery(PWD_QUERY);

				String secretKey = "";

				while(pwd.next()) {
					secretKey = pwd.getString(1);
				}

				if(secretKey.equals(password)) {
					success = Boolean.TRUE;
				}
			}

			conn.close();

			if(success)
			{
				returnObj.put("message", "success");
				returnObj.put("status_code", 200);
			}
			else
			{
				returnObj.put("message", "invalid credentials");
				returnObj.put("status_code", 401);
			}
			
		}catch(Exception e)
		{
			System.out.println(Throwables.getStackTraceAsString(e));
			returnObj = new JSONObject();
			returnObj.put("message", "internal server error");
			returnObj.put("status_code", 500);
		}
		return returnObj.toString();
	}
	
	@PutMapping("/user")
	public String updateUser(@RequestBody String id, @RequestBody String data) throws Exception {
		JSONObject returnObj = new JSONObject();
		try
		{
			Boolean success = Boolean.FALSE;
			//WRITE CODE HERE - Lets assume updates happen only for status and tokens
			/*
			 *  {
			 *     "registration_token":"24Zdsjbsdjadjadn283293",
			 *     "status":-1 //-1 : Negative, 0 - Symptomatic, 1 - Positive 
			 *  }
			 */

			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//((org.postgresql.PGConnection)conn).addDataType("geometry", PGgeometry.class);
			//((org.postgresql.PGConnection)conn).addDataType("geography", PGgeography.class);
			((org.postgresql.PGConnection)conn).addDataType("box3d", PGbox3d.class);
			Statement stmt = conn.createStatement();

			
			
			conn.close();

			if(success)
			{
				returnObj.put("message", "success");
				returnObj.put("status_code", 200);
			}
			else
			{
				throw new Exception();
			}
			
		}catch(Exception e)
		{
			System.out.println(Throwables.getStackTraceAsString(e));
			returnObj = new JSONObject();
			returnObj.put("message", "internal server error");
			returnObj.put("status_code", 500);
		}
		return returnObj.toString();
	}
	
	@GetMapping("/user")
	public String getUser(String id) throws Exception {
		JSONObject returnObj = new JSONObject();
		try
		{
			//WRITE CODE HERE
			//id - id column in usermaster
			//returns all columns except the password column in the usermaster table as a json converted to json
			//properties can be added in the json as follows
			//returnObj.put("key","value");

			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//((org.postgresql.PGConnection)conn).addDataType("geometry", PGgeometry.class);
			//((org.postgresql.PGConnection)conn).addDataType("geography", PGgeography.class);
			((org.postgresql.PGConnection)conn).addDataType("box3d", PGbox3d.class);
			Statement stmt = conn.createStatement();

			String TEMP = "DROP TABLE IF EXISTS \"Temp\";\n" +
				"CREATE TEMPORARY TABLE \"Temp\" AS SELECT * FROM \"UserMaster\" WHERE id = " + id + ";\n" +
				"ALTER TABLE \"Temp\" DROP COLUMN secret_key;\n";
            stmt.executeUpdate(TEMP);

            String ALL_QUERY = "SELECT * FROM \"Temp\";";
            ResultSet all = stmt.executeQuery(ALL_QUERY);
			while(all.next()) {
				long ID = all.getLong("id");
				String name = all.getString("name");
				String user_name = all.getString("user_name");
				String registration_token = all.getString("registration_token");
				String phone_number = all.getString("phone_number");
				String gender = all.getString("gender");
				int age = all.getInt("age");
				String state = all.getString("state");
				String city = all.getString("city");
				String status = all.getString("status");

				returnObj.put("id", ID);
				returnObj.put("name", name);
				returnObj.put("user_name", user_name);
				returnObj.put("registration_token", registration_token);
				returnObj.put("phone_number", phone_number);
				returnObj.put("gender", gender);
				returnObj.put("age", age);
				returnObj.put("state", state);
				returnObj.put("city", city);
				returnObj.put("status", status);
			}

            conn.close();
			
			returnObj.put("message", "success");
			returnObj.put("status_code", 200);
			
		}catch(Exception e)
		{
			System.out.println(Throwables.getStackTraceAsString(e));
			returnObj = new JSONObject();
			returnObj.put("message", "internal server error");
			returnObj.put("status_code", 500);
		}
		return returnObj.toString();
	}
	
	
	
	
}
