package com.example.accessingdatajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccessingDataJpaApplication {

  private static final Logger log = LoggerFactory.getLogger(AccessingDataJpaApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(AccessingDataJpaApplication.class, args);
  }

  @Bean
  public CommandLineRunner demo(CustomerRepository repository) {
    return (args) -> {
      // Save a few customers using your Customer constructor (iccid, customerEmail)
      repository.save(new Customer("ICCID-12345", "jack.bauer@example.com"));
      repository.save(new Customer("ICCID-54321", "chloe.obrian@example.com"));
      repository.save(new Customer("ICCID-67890", "kim.bauer@example.com"));
      repository.save(new Customer("ICCID-09876", "david.palmer@example.com"));
      repository.save(new Customer("ICCID-11223", "michelle.dessler@example.com"));

      // Fetch all customers
      log.info("Customers found with findAll():");
      log.info("-------------------------------");
      repository.findAll().forEach(customer -> log.info(customer.toString()));
      log.info("");

      // Fetch an individual customer by ID (assuming 1L exists)
      repository.findById(1L).ifPresent(customer -> {
        log.info("Customer found with findById(1L):");
        log.info("--------------------------------");
        log.info(customer.toString());
        log.info("");
      });

      // Fetch customers by customerEmail
      log.info("Customer found with findByCustomerEmail('jack.bauer@example.com'):");
      log.info("--------------------------------------------");
      repository.findByCustomerEmail("jack.bauer@example.com")
                .forEach(customer -> log.info(customer.toString()));
      log.info("");
    };
  }
}
