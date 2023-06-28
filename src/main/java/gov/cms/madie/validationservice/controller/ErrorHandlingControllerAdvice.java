package gov.cms.madie.validationservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import gov.cms.madie.validationservice.exception.InvalidFhirGroupException;
import gov.cms.madie.validationservice.exception.InvalidGroupException;
import gov.cms.madie.validationservice.exception.InvalidMeasureObservationException;
import gov.cms.madie.validationservice.exception.InvalidReturnTypeException;
import gov.cms.madie.validationservice.exception.InvalidReturnTypeForQdmException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

  @Autowired private final ErrorAttributes errorAttributes;

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, Object> onIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    return getErrorAttributes(request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidFhirGroupException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, Object> onInvalidFhirGroupException(
      InvalidFhirGroupException ex, WebRequest request) {
    return getErrorAttributes(request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidGroupException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, Object> onInvalidGroupException(InvalidGroupException ex, WebRequest request) {
    return getErrorAttributes(request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidReturnTypeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, Object> onInvalidReturnTypeException(
      InvalidReturnTypeException ex, WebRequest request) {
    return getErrorAttributes(request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidReturnTypeForQdmException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, Object> onInvalidReturnTypeForQdmException(
      InvalidReturnTypeForQdmException ex, WebRequest request) {
    return getErrorAttributes(request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidMeasureObservationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, Object> onInvalidMeasureObservationException(
      InvalidMeasureObservationException ex, WebRequest request) {
    return getErrorAttributes(request, HttpStatus.BAD_REQUEST);
  }

  private Map<String, Object> getErrorAttributes(WebRequest request, HttpStatus httpStatus) {
    // BINDING_ERRORS and STACK_TRACE are too detailed and confusing to parse
    // Let's just add a list of simplified validation errors
    ErrorAttributeOptions errorOptions =
        ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);
    Map<String, Object> errorAttributes =
        this.errorAttributes.getErrorAttributes(request, errorOptions);
    errorAttributes.put("status", httpStatus.value());
    errorAttributes.put("error", httpStatus.getReasonPhrase());
    return errorAttributes;
  }
}
