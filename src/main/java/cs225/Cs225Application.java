package cs225;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;

import api.Handler;

@SpringBootApplication
@ComponentScan(basePackageClasses = Handler.class)
public class Cs225Application {

	public static void main(String[] args) {
		SpringApplication.run(Cs225Application.class, args);
	}

    @Scheduled(cron = "0 * * * * *") //For now, runs every minute (Testing)
    public void notifyUsers() throws FirebaseMessagingException {
    	
    	//Process, fetch registeration_tokens and store it in the list below (registrationTokens)
    	
    	List<String> registrationTokens = Arrays.asList();

		MulticastMessage message = MulticastMessage.builder()
		    .putData("type", "notification")
		    .putData("message", "You have been in contact with a COVID symptomatic/positive person. Please report this and take precautionary measures")
		    .addAllTokens(registrationTokens)
		    .build();
		BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
		System.out.println(response.getSuccessCount() + " messages were sent successfully");
        
    }
}
