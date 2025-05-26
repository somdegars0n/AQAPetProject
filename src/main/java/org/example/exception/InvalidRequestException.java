package org.example.exception;

public class InvalidRequestException extends SenderException {
  public InvalidRequestException(String message) {
    super(message);
  }
}
