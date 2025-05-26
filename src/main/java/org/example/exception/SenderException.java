package org.example.exception;

public class SenderException extends RuntimeException{
  public SenderException(String message) {
    super(message);
  }

  public SenderException(String message, Throwable cause) {
    super(message, cause);
  }
}