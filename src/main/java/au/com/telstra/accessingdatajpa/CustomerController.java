package com.example.accessingdatajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    private RestTemplate restTemplate = new RestTemplate();

    // POST endpoint to create a new customer record
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequest request) {
        // Call the external SimCardActuator service using the ICCID
        String actuatorUrl = "http://localhost:8444/actuate";
        Map<String, String> payload = new HashMap<>();
        payload.put("iccid", request.getIccid());
        
        // Send POST request to actuator service
        ResponseEntity<Void> actuatorResponse = restTemplate.postForEntity(actuatorUrl, payload, Void.class);
        boolean activationSuccessful = actuatorResponse.getStatusCode().is2xxSuccessful();

        // Save a new record in the database with the activation status
        Customer newCustomer = new Customer(request.getIccid(), request.getCustomerEmail(), activationSuccessful);
        Customer savedCustomer = customerRepository.save(newCustomer);
        return ResponseEntity.ok(savedCustomer);
    }

    // GET endpoint to query a customer by simCardId (the customer's id)
    @GetMapping("/customer")
    public ResponseEntity<?> getCustomer(@RequestParam("simCardId") long simCardId) {
        return customerRepository.findById(simCardId)
                .map(customer -> {
                    // Build the response JSON object
                    Map<String, Object> response = new HashMap<>();
                    response.put("iccid", customer.getIccid());
                    response.put("customerEmail", customer.getCustomerEmail());
                    response.put("active", customer.isActive());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
