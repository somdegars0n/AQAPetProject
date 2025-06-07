package org.example;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

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
class StudentListTest {

    @Test
    void testStudentListOperations() {
        // 1. Создаем список студентов
        List<String> students = new ArrayList<>();

        // 2. Добавляем 5 имен
        students.add("Света");
        students.add("Антон");
        students.add("Настя");
        students.add("Слава");
        students.add("Катя");

        // Проверяем, что список содержит 5 элементов
        assertEquals(5, students.size());

        // 3. Проверяем вывод всех имен (имитируем вывод в консоль)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        for (String student : students) {
            System.out.println(student);
        }

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Света"));
        assertTrue(consoleOutput.contains("Антон"));
        assertTrue(consoleOutput.contains("Настя"));
        assertTrue(consoleOutput.contains("Слава"));
        assertTrue(consoleOutput.contains("Катя"));

        // 4. Удаляем второго студента (индекс 1)
        String removedStudent = students.remove(1);
        assertEquals("Антон", removedStudent);
        assertEquals(4, students.size());

        // 5. Проверяем наличие имени в списке
        assertTrue(students.contains("Слава"));
        assertFalse(students.contains("Антон")); // Так как мы его удалили

        // 6. Сортируем список по алфавиту
        Collections.sort(students);
        assertEquals("Катя", students.get(0));
        assertEquals("Настя", students.get(1));
        assertEquals("Света", students.get(2));
        assertEquals("Слава", students.get(3));

    }
}