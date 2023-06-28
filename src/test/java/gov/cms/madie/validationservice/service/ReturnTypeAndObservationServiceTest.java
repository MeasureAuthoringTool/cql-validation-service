package gov.cms.madie.validationservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.cms.madie.models.common.ModelType;
import gov.cms.madie.models.measure.Group;
import gov.cms.madie.models.measure.Measure;
import gov.cms.madie.models.measure.Population;
import gov.cms.madie.models.measure.PopulationType;
import gov.cms.madie.models.measure.QdmMeasure;
import gov.cms.madie.validationservice.validators.CqlDefinitionReturnTypeValidator;
import gov.cms.madie.validationservice.validators.CqlObservationFunctionValidator;

@ExtendWith(MockitoExtension.class)
public class ReturnTypeAndObservationServiceTest {

  @Mock private CqlDefinitionReturnTypeValidator cqlDefinitionReturnTypeValidator;
  @Mock private CqlObservationFunctionValidator cqlObservationFunctionValidator;

  @InjectMocks private ReturnTypeAndObservationService returnTypeAndObservationService;

  private Measure qiCoreMeasure;
  private Measure qdmMeasure;
  private Group group;

  @BeforeEach
  public void beforeEach() {
    qiCoreMeasure =
        Measure.builder().id("testMeasureId1").model(ModelType.QI_CORE.getValue()).build();

    qdmMeasure =
        QdmMeasure.builder()
            .model(ModelType.QDM_5_6.getValue())
            .patientBasis(false)
            .elmJson("testElmJson")
            .build();
    group =
        Group.builder()
            .id("testGroupId")
            .populationBasis("Encounter")
            .populations(
                List.of(
                    new Population(
                        "id-1", PopulationType.INITIAL_POPULATION, "boolipp", null, null)))
            .groupDescription("Description")
            .build();
  }

  @Test
  public void testValidateQiCoreSuccess() throws JsonProcessingException {
    doNothing()
        .when(cqlDefinitionReturnTypeValidator)
        .validateCqlDefinitionReturnTypes(any(Group.class), anyString());
    doNothing()
        .when(cqlObservationFunctionValidator)
        .validateObservationFunctions(any(Group.class), anyString());

    assertDoesNotThrow(
        () ->
            returnTypeAndObservationService.validateCqlDefinitionReturnTypesAndObservationFunctions(
                qiCoreMeasure, group, "testElmJson"));
  }

  @Test
  public void testValidateQdmSuccess() throws JsonProcessingException {

    when(cqlDefinitionReturnTypeValidator.validateCqlDefinitionReturnTypesForQdm(
            any(Group.class), anyString(), anyBoolean()))
        .thenReturn("testReturnType");
    doNothing()
        .when(cqlObservationFunctionValidator)
        .validateObservationFunctionsForQdm(
            any(Group.class), anyString(), anyBoolean(), anyString());

    assertDoesNotThrow(
        () ->
            returnTypeAndObservationService.validateCqlDefinitionReturnTypesAndObservationFunctions(
                qdmMeasure, group, "testElmJson"));
  }
}
