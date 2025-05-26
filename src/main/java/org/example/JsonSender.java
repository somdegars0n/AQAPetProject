package org.example;

public class JsonSender extends HttpSender{
  private final String jsonBody;

  public String getJsonBody() {
    return jsonBody;
  }

  public JsonSender(String url, String path, String jsonBody) {
    super(url, path);
    validateJson(jsonBody); // Проверка валидности JSON
    this.jsonBody = jsonBody != null ? jsonBody : "{}";
  }

  private void validateJson(String json) {
    if (json != null && !json.isEmpty()) {
      // Здесь может быть логика проверки валидности JSON
      if (!json.startsWith("{") && !json.startsWith("[")) {
        throw new IllegalArgumentException("Некорректный формат JSON");
      }
    }
  }

  @Override
  public String send(String url, String path, String body) {
    String actualUrl = (url != null && !url.isEmpty()) ? url : this.url;
    String actualPath = (path != null) ? path : this.path;
    String actualBody = (body != null) ? body : this.jsonBody;

    validateJson(actualBody);

    return String.format("По данному пути: %s%s отправлен JSON-запрос с телом: %s",
      actualUrl,
      actualPath,
      actualBody);
  }

  // Дополнительные методы для работы с JSON
  public String getPrettyJson() {
    // Здесь может быть логика для форматирования JSON
    return jsonBody.replace(",", ",\n").replace("{", "{\n").replace("}", "\n}");
  }
}