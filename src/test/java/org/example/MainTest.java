package org.example;

import org.example.Main;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void testMainMethodOutput() {
        // Сохраняем оригинальный System.out
        PrintStream originalOut = System.out;

        // Создаем поток для перехвата вывода
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Перенаправляем вывод в наш поток
            System.setOut(new PrintStream(outputStream));

            // Вызываем тестируемый метод
            Main.main(new String[]{"test"});

            // Ожидаемый вывод (с учетом System.lineSeparator())
            String expectedOutput = "Hello and welcome!" + System.lineSeparator() +
                    "i = 1" + System.lineSeparator() +
                    "i = 2" + System.lineSeparator() +
                    "i = 3" + System.lineSeparator() +
                    "i = 4" + System.lineSeparator();

            // Проверяем соответствие вывода
            assertEquals(expectedOutput, outputStream.toString());

        } finally {
            // Всегда восстанавливаем стандартный вывод
            System.setOut(originalOut);
        }
    }
}