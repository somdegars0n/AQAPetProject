package org.example;

import org.example.interfaces.Sender;

import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

  public static void main(String[] args) {
    // Press Alt+Enter with your caret at the highlighted text to see how
    // IntelliJ IDEA suggests fixing it.
    System.out.println("Hello and welcome!");

//    cycle(10);

    // Создаем базовые отправители
    Sender textSender = new TextSender("example.com", "/api", "Hello, world!");
    Sender jsonSender = new JsonSender("api.example.com", "/users",
      "{\"name\":\"John\",\"age\":30}");

    // Используем полиморфизм - работаем через общий интерфейс
    System.out.println("Текстовый отправитель: " + textSender.send());
    System.out.println("JSON отправитель: " + jsonSender.send());

    // Добавляем логирование (декоратор)
    Sender loggingTextSender = new LoggingSender(textSender);
    loggingTextSender.send("log.example.com", "/logs", "Важное сообщение");

    // Добавляем шифрование (еще один декоратор)
    Sender encryptedJsonSender = new EncryptedSender(jsonSender, "secret-key");
    System.out.println(encryptedJsonSender.send());

    // Комбинируем декораторы - логирование + шифрование
    Sender secureLoggingSender = new LoggingSender(new EncryptedSender(textSender, "another-key"));
    secureLoggingSender.send("secure.example.com", "/secure-api", "Секретные данные");
  }

  // метод cycle_for
  public static List<Integer> cycle(int count) {
    List<Integer> listNumbers = new ArrayList<>(count);

    for (int i = 0; i < count; i++) {

      // Press Shift+F9 to start debugging your code. We have set one breakpoint
      // for you, but you can always add more by pressing Ctrl+F8.
//      System.out.println("i = " + i);
      listNumbers.add(i);
//      if (i % 2 == 0) {
//        System.out.println("even");
//      } else if (i % 2 == 1) {
//        System.out.println("not-even");
//      } else {
//        System.out.println("odd");
//      }
//      String result = (i % 2 == 0) ? "even" : "not-even";
//      System.out.println(result);
    }
    System.out.println("listNumbers = " + listNumbers);

    return listNumbers;
  }
}