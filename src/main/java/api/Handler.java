package api;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class Handler {

	@PostMapping("/upload")
	public String dumpData(@RequestBody String data) throws Exception {
		JSONObject obj = new JSONObject(data);
		
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
