package org.example.interfaces;

public interface Logger {
  void logRequest(String url, String path, String body);
  void logRequest(String url, String path);
  void logResponse(String response);
}