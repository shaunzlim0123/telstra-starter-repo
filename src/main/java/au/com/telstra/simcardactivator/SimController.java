package au.com.telstra.simcardactivator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SimController {

	private final RestTemplate restTemplate = new RestTemplate();
	private final String actuatorUrl = "http://localhost:8444/actuate";

	@PostMapping("/activate")
	public String activateSim(@RequestBody Sim sim) {
        String iccid = sim.getIccid();

        // Create an ActuatorRequest object; Spring Boot will convert this to JSON automatically.
        ActuatorRequest actuatorRequest = new ActuatorRequest(iccid);
        
        // Send the POST request to the actuator microservice.
        ResponseEntity<String> response = restTemplate.postForEntity(
            actuatorUrl, actuatorRequest, String.class);

        // Return the actuator's response
        return "Actuator response: " + response.getBody();
	}
}