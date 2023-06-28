package gov.cms.madie.validationservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import gov.cms.madie.validationservice.controller.ReturnTypeAndObservationValidationController;
import lombok.Generated;

@SpringBootTest
@EnableAutoConfiguration
public class ValidationServiceApplicationTest {

  @Autowired private ReturnTypeAndObservationValidationController controller;

  @Generated
  public static void main(String[] args) {
    SpringApplication.run(ValidationServiceApplication.class, args);
  }

  @Test
  public void contextLoads() {
    assertNotNull(controller);
  }
}
