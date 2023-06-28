package gov.cms.madie.validationservice.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.cms.madie.models.common.ModelType;
import gov.cms.madie.models.measure.Group;
import gov.cms.madie.models.measure.Measure;
import gov.cms.madie.models.measure.Population;
import gov.cms.madie.models.measure.PopulationType;
import gov.cms.madie.validationservice.service.ReturnTypeAndObservationService;

@ExtendWith(MockitoExtension.class)
public class ReturnTypeAndObservationValidationControllerTest {

  @Mock private ReturnTypeAndObservationService returnTypeAndObservationService;

  @InjectMocks private ReturnTypeAndObservationValidationController controller;

  private Measure measure;
  private Group group;

  @BeforeEach
  public void setUp() {
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
    measure =
        Measure.builder()
            .id("testMeasureId1")
            .model(ModelType.QI_CORE.getValue())
            .groups(List.of(group))
            .elmJson("testElmJson")
            .build();
    ;
  }

  @Test
  public void testValidateReturnTypesAndObservation() throws JsonProcessingException {
    doNothing()
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    ResponseEntity<Measure> validateMeasure = controller.validateReturnTypesAndObservation(measure);

    assertNotNull(validateMeasure);
  }

  @Test
  public void testValidateReturnTypesAndObservationThrowsJsonProcessingException()
      throws JsonProcessingException {
    doThrow(JsonProcessingException.class)
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    assertThrows(
        IllegalArgumentException.class,
        () -> controller.validateReturnTypesAndObservation(measure));
  }
}
