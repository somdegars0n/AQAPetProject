package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class SenderTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String DEFAULT_PATH = "/posts";
    private static final Gson gson = new Gson();

    // Создаем JSON с помощью Gson
    private static final String DEFAULT_BODY = createDefaultBody();

    private TextSender sender;

    // Вспомогательный метод для создания JSON
    private static String createDefaultBody() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", "foo");
        jsonObject.addProperty("body", "bar");
        jsonObject.addProperty("userId", 1);
        return gson.toJson(jsonObject);
    }

    @BeforeEach
    void setUp() {
        sender = new TextSender(BASE_URL, DEFAULT_PATH, DEFAULT_BODY);
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Тест интеграции с JSONPlaceholder")
    void testSenderIntegrationWithRealApiPost() {
        // Форматирование результата Sender для получения информации о запросе
        String senderResult = sender.send(BASE_URL, "/posts", DEFAULT_BODY);
        System.out.println("Sender result: " + senderResult);

        // Теперь выполним реальный HTTP-запрос с теми же параметрами
        Response response = given()
          .contentType(ContentType.JSON)
          .body(DEFAULT_BODY)
          .when()
          .post("/posts")
          .then()
          .extract().response();

        System.out.println("Response statusCode: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
        // Проверяем, что запрос успешно выполнен
        assertEquals(201, response.statusCode(), "Статус код должен быть 201 для POST");

        // Преобразуем JSON ответа в объект с помощью Gson для проверки
        String responseBody = response.getBody().asString();
        JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

        // Проверяем, что ответ содержит отправленные данные
        assertEquals("foo", responseJson.get("title").getAsString());
        assertEquals("bar", responseJson.get("body").getAsString());
        assertEquals(1, responseJson.get("userId").getAsInt());

        assertEquals(101, responseJson.get("id").getAsInt(), "id не соответствует ожидаемому");

        // Проверяем, что информация о запросе, предоставленная Sender,
        // соответствует фактически отправленному запросу
        assertTrue(senderResult.contains(BASE_URL), "URL должен совпадать");
        assertTrue(senderResult.contains("/posts"), "Path должен совпадать");
        assertTrue(senderResult.contains(DEFAULT_BODY.trim()), "Body должен совпадать");
    }


    @Test
    @DisplayName("Тест интеграции с JSONPlaceholder - получение поста с Gson")
    void testSenderIntegrationWithRealApiGet() {
        // Создаем путь для получения конкретного поста
        String specificPath = "/posts/1";

        String schemaPath = "schemas/response/api/post-single-schema.json";
        // Форматирование результата Sender
        String senderResult = sender.send(BASE_URL, specificPath, "");
        System.out.println("Sender result: " + senderResult);

        // Выполним реальный HTTP-запрос
        Response response = given()
          .when()
          .get(specificPath)
          .then()
          .assertThat()
          .statusCode(200)
          // Добавляем проверку схемы JSON, если путь к схеме указан
          .and()
          .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath))
          .extract().response();

        // Преобразуем JSON ответа в объект с помощью Gson
        String responseBody = response.getBody().asString();
        JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

        // Проверки ответа API
        assertEquals(200, response.statusCode(), "Статус код должен быть 200 для GET");
        assertEquals(1, responseJson.get("id").getAsInt(), "Id должен быть 1");
        assertNotNull(responseJson.get("title").getAsString(), "Title не должен быть null");

        // Проверка информации из Sender
        assertTrue(senderResult.contains(BASE_URL), "URL должен совпадать");
        assertTrue(senderResult.contains(specificPath), "Path должен совпадать");
    }

    @Test
    @DisplayName("Тест интеграции с JSONPlaceholder - обновление поста с Gson")
    void testSenderIntegrationWithRealApiPut() {
        String updatePath = "/posts/1";

        // Создаем JSON для обновления с помощью Gson
        JsonObject updateJson = new JsonObject();
        updateJson.addProperty("id", 1);
        updateJson.addProperty("title", "updated title");
        updateJson.addProperty("body", "updated body");
        updateJson.addProperty("userId", 1);
        String updateBody = gson.toJson(updateJson);

        // Форматирование результата Sender
        String senderResult = sender.send(BASE_URL, updatePath, updateBody);
        System.out.println("Sender result: " + senderResult);

        // Выполним реальный HTTP-запрос
        Response response = given()
          .contentType(ContentType.JSON)
          .body(updateBody)
          .when()
          .put(updatePath)
          .then()
          .extract().response();

        // Преобразуем JSON ответа в объект с помощью Gson
        String responseBody = response.getBody().asString();
        JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

        // Проверки ответа API
        assertEquals(200, response.statusCode(), "Статус код должен быть 200 для PUT");
        assertEquals("updated title", responseJson.get("title").getAsString());
        assertEquals("updated body", responseJson.get("body").getAsString());

        // Проверка информации из Sender
        assertTrue(senderResult.contains(BASE_URL), "URL должен совпадать");
        assertTrue(senderResult.contains(updatePath), "Path должен совпадать");
        assertTrue(senderResult.contains(updateBody.trim()), "Body должен совпадать");
    }

    @Test
    @DisplayName("Тест интеграции с JSONPlaceholder - фильтрация постов с Gson")
    void testSenderIntegrationWithRealApiQueryParams() {
        String queryPath = "/posts?userId=1";

        // Форматирование результата Sender
        String senderResult = sender.send(BASE_URL, queryPath, "");
        System.out.println("Sender result: " + senderResult);

        // Выполним реальный HTTP-запрос с query параметрами
        Response response = given()
          .param("userId", 1)
          .when()
          .get("/posts")
          .then()
          .extract().response();

        // Преобразуем JSON ответа в массив объектов с помощью Gson
        String responseBody = response.getBody().asString();
        Object[] posts = gson.fromJson(responseBody, Object[].class);

        // Проверки ответа API
        assertEquals(200, response.statusCode(), "Статус код должен быть 200 для GET с параметрами");
        assertTrue(posts.length > 0, "Должны быть найдены посты");

        // Проверка информации из Sender
        assertTrue(senderResult.contains(BASE_URL), "URL должен совпадать");
        assertTrue(senderResult.contains(queryPath), "Path с query параметрами должен совпадать");
    }

    @ParameterizedTest
    @MethodSource("provideEndpointsForTesting")
    @DisplayName("Параметризованный тест различных эндпоинтов JSONPlaceholder с Gson")
    void testMultipleEndpoints(String path, String requestBody, int expectedStatusCode, String schemaPath) {
        // Форматирование результата Sender
        String senderResult = sender.send(BASE_URL, path, requestBody != null ? requestBody : "");
        System.out.println("Sender result for " + path + ": " + senderResult);

        // Определим HTTP метод на основе пути и тела запроса
        Response response;
        if (requestBody != null && !requestBody.isEmpty()) {
            // POST запрос с телом
            response = given()
              .contentType(ContentType.JSON)
              .body(requestBody)
              .when()
              .post(path)
              .then()
              .assertThat()
              .statusCode(expectedStatusCode)
              // Добавляем проверку схемы JSON, если путь к схеме указан
              .and()
              .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath))
              .extract().response();
        } else {
            // GET запрос
            response = given()
              .when()
              .get(path)
              .then()
              .assertThat()
              .statusCode(expectedStatusCode)
              .extract().response();
        }

        // Проверки ответа API
        assertEquals(expectedStatusCode, response.statusCode(),
          "Статус код должен быть " + expectedStatusCode + " для " + path);

        // Проверка информации из Sender
        assertTrue(senderResult.contains(BASE_URL), "URL должен совпадать");
        assertTrue(senderResult.contains(path), "Path должен совпадать");
        if (requestBody != null && !requestBody.isEmpty()) {
            assertTrue(senderResult.contains(requestBody.trim()), "Body должен совпадать");
        }
    }

    static Stream<Arguments> provideEndpointsForTesting() {
        // Создаем JSON для POST-запроса
        JsonObject postJson = new JsonObject();
        postJson.addProperty("title", "test post");
        postJson.addProperty("body", "test body");
        postJson.addProperty("userId", 1);
        String postBody = new Gson().toJson(postJson);

        return Stream.of(
          Arguments.of("/posts", null, 200, "schemas/response/api/posts-list-schema.json"),
          Arguments.of("/posts/1", null, 200, "schemas/response/api/post-single-schema.json"),
          Arguments.of("/users", null, 200, "schemas/response/api/users-list-schema.json"),
          Arguments.of("/users/1", null, 200, "schemas/response/api/user-single-schema.json"),
          Arguments.of("/comments?postId=1", null, 200, "schemas/response/api/comments-list-schema.json"),
          Arguments.of("/posts", postBody, 201, "schemas/response/api/post-created-schema.json")
        );
    }

    // Добавлены новые тесты, нацеленные на работу с JSON и с сайтом jsonplaceholder


    // тест с падением на 1ой ошибке
    @Test
    void testSendWithConditions() {
        TextSender sender = new TextSender("example.com", "/api", "default body");
        String result = sender.send("https://github.com", "/ViktoriiaBelousova/TestAQA", "body");

        // Проверка с if-else
        if (!result.contains("https://github.com")) {
            fail("URL не совпадает");
        } else if (!result.contains("/ViktoriiaBelousova/TestAQA")) {
            fail("Path не совпадает");
        } else if (!result.contains("body")) {
            fail("Body не совпадает");
        } else {
            System.out.println("Все части ответа корректны!");
        }
    }

    //тест когда собираем все ошибки(contains состоит из.. частичное совпадение)
    @Test
    void testSendWithAllErrorsReported() {
        TextSender sender = new TextSender("example.com", "/api", "default body");
        String result = sender.send("https://github.com", "/ViktoriiaBelousova/TestAQA", "body");

        StringBuilder errors = new StringBuilder();

        // Проверяем все условия и собираем ошибки "https://github.com", "/ViktoriiaBelousova/TestAQA", "body"));
        if (!result.contains("https://github.com")) {
            errors.append("URL не совпадает\n");
        }

        if (!result.contains("/ViktoriiaBelousova/TestAQA")) {
            errors.append("Path не совпадает\n");
        }

        if (!result.contains("body")) {
            errors.append("Body не совпадает\n");
        }

        // Если есть ошибки - выводим все разом
        if (!errors.isEmpty()) {
            fail("Найдены ошибки:\n" + errors);
        } else {
            System.out.println("Все части ответа корректны!");
        }
    }

    //проверка абсолютного совпадения
    @Test
    void testSendWithExactMatch() {
        TextSender sender = new TextSender("default.com", "/api", "default body");

        // Ожидаемый результат
        String expected = "По данному пути: https://github.com/ViktoriiaBelousova/TestAQA , отправлен запрос с телом: body";

        // Фактический результат
        String actual = sender.send("https://github.com", "/ViktoriiaBelousova/TestAQA", "body");

        // Проверка абсолютного совпадения
        assertEquals(expected, actual);
    }
}