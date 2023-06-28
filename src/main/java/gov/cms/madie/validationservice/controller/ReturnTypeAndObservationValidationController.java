package gov.cms.madie.validationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.cms.madie.models.measure.Measure;
import gov.cms.madie.validationservice.service.ReturnTypeAndObservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/validation")
public class ReturnTypeAndObservationValidationController {

  private final ReturnTypeAndObservationService qiCoreReturnTypeService;

  @PostMapping("/returntypes")
  public ResponseEntity<Measure> validateReturnTypesAndObservation(@RequestBody Measure measure) {
    log.info("validateReturnTypesAndObservation(): measure.getId() = " + measure.getId());

    try {
      qiCoreReturnTypeService.validateCqlDefinitionReturnTypesAndObservationFunctions(
          measure, measure.getGroups().get(0), measure.getElmJson());
      log.info("valid return types and observation function");
      return ResponseEntity.status(HttpStatus.OK).body(measure);
    } catch (JsonProcessingException ex) {

      log.error(
          "An error occurred while validating population "
              + "definition return types for measure {}",
          measure.getId(),
          ex);
      throw new IllegalArgumentException("Invalid elm json");
    }
  }
}
