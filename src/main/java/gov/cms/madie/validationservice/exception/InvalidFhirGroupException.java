package gov.cms.madie.validationservice.exception;

public class InvalidFhirGroupException extends RuntimeException {

  private static final long serialVersionUID = 5307493610722480826L;
  private static final String MESSAGE =
      "Measure Group Types and Population Basis are required for FHIR Measure Group";

  public InvalidFhirGroupException() {
    super(MESSAGE);
  }
}
