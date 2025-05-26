package org.example;

import org.example.interfaces.Logger;
import org.example.interfaces.Sender;

public class LoggingSender implements Sender, Logger {
  private final Sender wrappedSender;

  public LoggingSender(Sender wrappedSender) {
    this.wrappedSender = wrappedSender;
  }

  @Override
  public String send(String url, String path, String body) {
    logRequest(url, path, body);
    String response = wrappedSender.send(url, path, body);
    logResponse(response);
    return response;
  }

  @Override
  public String send(String url, String path) {
    logRequest(url, path);
    String response = wrappedSender.send(url, path);
    logResponse(response);
    return response;
  }

  @Override
  public String send(String url) {
    String response = wrappedSender.send(url);
    logResponse(response);
    return response;
  }

  @Override
  public String send() {
    return wrappedSender.send();
  }

  @Override
  public void logRequest(String url, String path, String body) {
    System.out.println("ЗАПРОС: URL=" + url + ", Path=" + path + ", Body=" + body);
  }

  @Override
  public void logRequest(String url, String path) {
    System.out.println("ЗАПРОС: URL=" + url + ", Path=" + path);
  }

  @Override
  public void logResponse(String response) {
    System.out.println("ОТВЕТ: " + response);
  }
}