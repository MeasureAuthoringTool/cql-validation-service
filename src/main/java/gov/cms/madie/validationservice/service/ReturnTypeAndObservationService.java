package gov.cms.madie.validationservice.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.cms.madie.models.common.ModelType;
import gov.cms.madie.models.measure.Group;
import gov.cms.madie.models.measure.Measure;
import gov.cms.madie.models.measure.QdmMeasure;
import gov.cms.madie.validationservice.validators.CqlDefinitionReturnTypeValidator;
import gov.cms.madie.validationservice.validators.CqlObservationFunctionValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ReturnTypeAndObservationService {

  private CqlDefinitionReturnTypeValidator cqlDefinitionReturnTypeValidator;
  private CqlObservationFunctionValidator cqlObservationFunctionValidator;

  public void validateCqlDefinitionReturnTypesAndObservationFunctions(
      Measure measure, Group group, String elmJson) throws JsonProcessingException {
    log.debug(
        "validateCqlDefinitionReturnTypesAndObservationFunctions for measure: " + measure.getId());
    if (ModelType.QI_CORE.getValue().equalsIgnoreCase(measure.getModel())) {
      cqlDefinitionReturnTypeValidator.validateCqlDefinitionReturnTypes(group, elmJson);
      cqlObservationFunctionValidator.validateObservationFunctions(group, elmJson);
    } else if (ModelType.QDM_5_6.getValue().equalsIgnoreCase(measure.getModel())) {
      QdmMeasure qdmMeasure = (QdmMeasure) measure;
      log.debug(
          "qdmMeasure isPatientBasis = "
              + qdmMeasure.isPatientBasis()
              + " scoring = "
              + qdmMeasure.getScoring());
      String cqlDefinitionReturnType =
          cqlDefinitionReturnTypeValidator.validateCqlDefinitionReturnTypesForQdm(
              group, elmJson, qdmMeasure.isPatientBasis());
      cqlObservationFunctionValidator.validateObservationFunctionsForQdm(
          group, elmJson, qdmMeasure.isPatientBasis(), cqlDefinitionReturnType);
    }
  }
}
