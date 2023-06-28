package gov.cms.madie.validationservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import gov.cms.madie.validationservice.config.SecurityConfig;
import gov.cms.madie.models.common.ModelType;
import gov.cms.madie.models.measure.Group;
import gov.cms.madie.models.measure.Measure;
import gov.cms.madie.models.measure.Population;
import gov.cms.madie.models.measure.PopulationType;
import gov.cms.madie.validationservice.exception.InvalidFhirGroupException;
import gov.cms.madie.validationservice.exception.InvalidGroupException;
import gov.cms.madie.validationservice.exception.InvalidMeasureObservationException;
import gov.cms.madie.validationservice.exception.InvalidReturnTypeException;
import gov.cms.madie.validationservice.exception.InvalidReturnTypeForQdmException;
import gov.cms.madie.validationservice.service.ReturnTypeAndObservationService;

@WebMvcTest({ReturnTypeAndObservationValidationController.class})
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class ReturnTypeAndObservationValidationControllerMvcTest {

  @MockBean private ReturnTypeAndObservationService returnTypeAndObservationService;

  @Autowired private MockMvc mockMvc;

  private static final String TEST_USER_ID = "test-okta-user-id-123";

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
  public void testValidateReturnTypesAndObservationSuccess() throws Exception {
    doNothing()
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    final String measureAsJson = toJsonString(measure);
    mockMvc
        .perform(
            post("/validation/returntypes")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .header("Authorization", "test-okta")
                .content(measureAsJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  @Test
  public void testValidateReturnTypesAndObservationThrowsIllegalArgumentException()
      throws Exception {
    doThrow(new IllegalArgumentException("No definitions found."))
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    final String measureAsJson = toJsonString(measure);
    mockMvc
        .perform(
            post("/validation/returntypes")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .header("Authorization", "test-okta")
                .content(measureAsJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("No definitions found."));
  }

  @Test
  public void testValidateReturnTypesAndObservationThrowsInvalidFhirGroupException()
      throws Exception {

    doThrow(new InvalidFhirGroupException())
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    final String measureAsJson = toJsonString(measure);
    mockMvc
        .perform(
            post("/validation/returntypes")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .header("Authorization", "test-okta")
                .content(measureAsJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testValidateReturnTypesAndObservationThrowsInvalidGroupException() throws Exception {

    doThrow(new InvalidGroupException("Populations are required for a Group."))
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    final String measureAsJson = toJsonString(measure);
    mockMvc
        .perform(
            post("/validation/returntypes")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .header("Authorization", "test-okta")
                .content(measureAsJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Populations are required for a Group."));
  }

  @Test
  public void testValidateReturnTypesAndObservationThrowsInvalidReturnTypeException()
      throws Exception {

    doThrow(new InvalidReturnTypeException("boolipp"))
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    final String measureAsJson = toJsonString(measure);
    mockMvc
        .perform(
            post("/validation/returntypes")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .header("Authorization", "test-okta")
                .content(measureAsJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Return type for the CQL definition selected for the boolipp does not match with population basis."));
  }

  @Test
  public void testValidateReturnTypesAndObservationThrowsInvalidReturnTypeForQdmException()
      throws Exception {

    doThrow(
            new InvalidReturnTypeForQdmException(
                "For Episode-based Measures, selected definitions must return a list of the same type (Non-Boolean)."))
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    final String measureAsJson = toJsonString(measure);
    mockMvc
        .perform(
            post("/validation/returntypes")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .header("Authorization", "test-okta")
                .content(measureAsJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message")
                .value(
                    "For Episode-based Measures, selected definitions must return a list of the same type (Non-Boolean)."));
  }

  @Test
  public void testValidateReturnTypesAndObservationThrowsInvalidMeasureObservationException()
      throws Exception {

    doThrow(
            new InvalidMeasureObservationException(
                "Measure CQL does not have observation definition"))
        .when(returnTypeAndObservationService)
        .validateCqlDefinitionReturnTypesAndObservationFunctions(
            any(Measure.class), any(Group.class), anyString());

    final String measureAsJson = toJsonString(measure);
    mockMvc
        .perform(
            post("/validation/returntypes")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .header("Authorization", "test-okta")
                .content(measureAsJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Measure CQL does not have observation definition"));
  }

  public String toJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return mapper.writeValueAsString(obj);
  }
}
