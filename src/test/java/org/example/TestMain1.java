package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestMain1 {

    @Test
    public void testCycle() {
        List<Integer> expectedList = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
        List<Integer> actualList = Main.cycle(7);
        assertEquals(expectedList, actualList, "cycle() должен возвращать числа от 0 до 6");
    }

    @Test
    public void testCycleOutput() {
        // Сохраняем оригинальный System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Перенаправляем вывод в наш поток
            System.setOut(new PrintStream(outputStream));

            // Вызываем тестируемый метод
            Main.cycle(4);

            // Ожидаемый вывод
            String expectedOutput = "i = 1\r\ni = 2\r\ni = 3\r\ni = 4\r\n";

            // Проверяем соответствие вывода
            assertEquals(expectedOutput, outputStream.toString());

        } catch (Exception e) {
            // Если возникло исключение - помечаем тест как проваленный
            fail("Тест выбросил исключение: " + e.getMessage());

        } finally {
            // Всегда восстанавливаем стандартный вывод
            System.setOut(originalOut);
        }
    }
}