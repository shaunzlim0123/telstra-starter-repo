package au.com.telstra.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class SimCardActivatorStepDefinitions {

    // Provided RestTemplate for submitting requests to our microservice at localhost:8080
    private RestTemplate restTemplate = new RestTemplate();

    private String iccid;
    private String customerEmail;
    private ResponseEntity<Map> postResponse;
    private long createdId;

    @Given("a SIM card with ICCID {string} and customer email {string}")
    public void a_sim_card_with_iccid_and_customer_email(String iccid, String customerEmail) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
    }

    @When("the activation request is submitted")
    public void the_activation_request_is_submitted() {
        // URL of our microservice POST endpoint
        String url = "http://localhost:8080/customers";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("iccid", iccid);
        requestBody.put("customerEmail", customerEmail);

        // Submit the POST request. We expect the microservice to return a JSON representing the saved Customer.
        postResponse = restTemplate.postForEntity(url, requestBody, Map.class);

        // Extract the created record's ID from the response for later verification.
        Map responseBody = postResponse.getBody();
        assertNotNull("Response body should not be null", responseBody);
        assertNotNull("Expected an 'id' field in the response", responseBody.get("id"));
        createdId = ((Number) responseBody.get("id")).longValue();
    }

    @Then("the SIM card activation should be successful")
    public void the_sim_card_activation_should_be_successful() {
        Map responseBody = postResponse.getBody();
        assertNotNull("Response body should not be null", responseBody);
        Boolean active = (Boolean) responseBody.get("active");
        assertNotNull("Expected 'active' field in the response", active);
        assertTrue("Expected activation to be successful", active);
    }

    @Then("the SIM card activation should fail")
    public void the_sim_card_activation_should_fail() {
        Map responseBody = postResponse.getBody();
        assertNotNull("Response body should not be null", responseBody);
        Boolean active = (Boolean) responseBody.get("active");
        assertNotNull("Expected 'active' field in the response", active);
        assertFalse("Expected activation to fail", active);
    }

    @Then("the activation record should have ICCID {string}, customerEmail {string}, and active {string}")
    public void the_activation_record_should_have_iccid_customer_email_and_active(String expectedIccid, String expectedCustomerEmail, String expectedActiveStr) {
        boolean expectedActive = Boolean.parseBoolean(expectedActiveStr);
        // Use the GET endpoint to query the activation record using the stored createdId.
        String url = "http://localhost:8080/customer?simCardId=" + createdId;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map responseBody = response.getBody();
        assertNotNull("Expected to retrieve an activation record", responseBody);
        assertEquals("ICCID does not match", expectedIccid, responseBody.get("iccid"));
        assertEquals("Customer email does not match", expectedCustomerEmail, responseBody.get("customerEmail"));
        assertEquals("Activation status does not match", expectedActive, responseBody.get("active"));
    }
}
