package gov.cms.madie.validationservice.exception;

public class InvalidGroupException extends RuntimeException {

  private static final long serialVersionUID = -244725442033685561L;

  public InvalidGroupException(String message) {
    super(message);
  }
}
