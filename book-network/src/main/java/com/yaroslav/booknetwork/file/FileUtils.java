package com.yaroslav.booknetwork.file;

import lombok.extern.slf4j.Slf4j; // Аннотация, которая добавляет логгирование в класс
import org.apache.commons.lang3.StringUtils; // Утилиты для работы со строками

import java.io.File; // Класс File для работы с файловой системой
import java.io.IOException; // Исключение, возникающее при ошибках ввода-вывода
import java.nio.file.Files; // Класс Files для работы с файлами
import java.nio.file.Path; // Класс Path для представления пути к файлу

@Slf4j // Lombok аннотация, которая добавляет поле логгера в класс
public class FileUtils {

    public static byte[] readFileFromLocation(String fileUrl) { // Метод для чтения файла из заданного местоположения
        if (StringUtils.isBlank(fileUrl)) { // Проверяем, что строка не пустая или не содержит только пробелы
            return null; // Если строка пуста, возвращаем null
        }
        try {
            Path filePath = new File(fileUrl).toPath(); // Преобразуем строку пути в объект Path
            return Files.readAllBytes(filePath); // Считываем все байты из файла
        } catch (IOException e) { // Обработка исключения ввода-вывода
            log.warn("No file found in the path {} {}", fileUrl, e); // Логгируем предупреждение, если файл не найден или возникла другая ошибка
        }
        return null; // Если произошла ошибка, возвращаем null
    }
}

