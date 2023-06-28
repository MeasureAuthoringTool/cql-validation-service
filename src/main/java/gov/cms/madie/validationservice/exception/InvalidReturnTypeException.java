package gov.cms.madie.validationservice.exception;

public class InvalidReturnTypeException extends RuntimeException {

  private static final long serialVersionUID = -4524586311863193462L;
  private static final String MESSAGE =
      "Return type for the CQL definition selected for the %s "
          + "does not match with population basis.";

  public InvalidReturnTypeException(String message, String observationDisplayDetails) {
    super(String.format(message, observationDisplayDetails));
  }

  public InvalidReturnTypeException(String populationName) {
    super(String.format(MESSAGE, populationName));
  }
}
