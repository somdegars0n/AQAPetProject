package org.example;

public class TextSender extends HttpSender {

  private final String body;

  public String getBody() {
    return body;
  }

  public TextSender(String url, String path, String body) {
    super(url, path);
    this.body = body != null ? body : "";
  }

  @Override
  public String send(String url, String path, String body) {
    // Используем переданные параметры или значения по умолчанию из полей класса
    String actualUrl = (url != null && !url.isEmpty()) ? url : this.url;
    String actualPath = (path != null) ? path : this.path;
    String actualBody = (body != null) ? body : this.body;

    // Формируем результирующую строку
    return String.format("По данному пути: %s%s отправлен запрос с телом: %s",
      actualUrl,
      actualPath,
      actualBody);
  }
}