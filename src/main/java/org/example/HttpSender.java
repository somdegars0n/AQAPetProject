package org.example;

import org.example.interfaces.Sender;

public abstract class HttpSender implements Sender {
  protected final String url;
  protected final String path;

  public HttpSender(String url, String path) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("URL не может быть пустым");
    }
    if (path == null) {
      throw new IllegalArgumentException("Path не может быть null");
    }

    this.url = url;
    this.path = path;
  }

  public String getUrl() {
    return url;
  }

  public String getPath() {
    return path;
  }

  @Override
  public abstract String send(String url, String path, String body);

  // Перегруженные методы send
  @Override
  public String send(String url, String path) {
    return send(url, path, null);
  }

  @Override
  public String send(String url) {
    return send(url, null, null);
  }

  @Override
  public String send() {
    return send(null, null, null);
  }

  // Общий метод для всех наследников
  protected String formatUrl(String url, String path) {
    return url + path;
  }
}