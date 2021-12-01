package cs225;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;

import api.Handler;

@SpringBootApplication
@ComponentScan(basePackageClasses = Handler.class)
@EnableScheduling
public class Cs225Application {
	
	private static Boolean isInitCalled = Boolean.FALSE;

	public static void main(String[] args) throws FirebaseMessagingException, IOException {
		SpringApplication.run(Cs225Application.class, args);
		init();
	}

	private static void init() throws FirebaseMessagingException, IOException {
		
		isInitCalled = Boolean.TRUE;
		System.out.println("Init called");
		FileInputStream serviceAccount =
				  new FileInputStream("cs225-middleware-firebase-adminsdk-v3hq4-3aa89a208a.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .build();

		FirebaseApp.initializeApp(options);
		System.out.println("Init Completed");
	}
	
    @Scheduled(fixedRate = 3600000) //For now, runs every minute (Testing)
    public void notifyUsers() throws FirebaseMessagingException, IOException {
    	
    	if(!isInitCalled)
    	{
    		init();
    	}
    	
    	System.out.println("Inside Job");
    	//Process, fetch registeration_tokens and store it in the list below (registrationTokens)
    	
    	List<String> registrationTokens = Handler.getRegisterationTokensForScheduler();
    	
//    	registrationTokens = Arrays.asList(
//    			"faW9g2JnQRSXPG6CV6IntC:APA91bEeg216szQjCmN42TcXLopOujaraCQfNtl10IqRAa8g3f0HyLumK6_EiEQA1lYCw9pWb16Wdv9ps3AIKaVCQi3xG8cx7kTN9tLCukV19GAptX6eU3UplYL0Exsfm38e2h58ebD2"
//    	);

		MulticastMessage message = MulticastMessage.builder()
		    .putData("type", "notification")
		    .putData("message", "You have been in contact with a COVID symptomatic/positive person. Please report this and take precautionary measures.")
		    .addAllTokens(registrationTokens)
		    .build();
		BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
		System.out.println(response.getSuccessCount() + " messages were sent successfully");
        
    }
}
