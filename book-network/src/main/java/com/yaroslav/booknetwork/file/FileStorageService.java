package com.yaroslav.booknetwork.file;

// Импорт необходимых классов и аннотаций
import lombok.NonNull; // Аннотация Lombok, которая указывает, что поле или аргумент не может быть null
import lombok.RequiredArgsConstructor; // Lombok создает конструктор, принимающий все поля, помеченные как final или @NonNull
import lombok.extern.slf4j.Slf4j; // Добавляет поддержку логгирования с использованием SLF4J
import org.springframework.beans.factory.annotation.Value; // Позволяет читать значения из файла настроек
import org.springframework.stereotype.Service; // Аннотация, обозначающая, что класс является сервисом
import org.springframework.web.multipart.MultipartFile; // Интерфейс для работы с загружаемыми файлами

import java.io.File; // Для работы с файлами в файловой системе
import java.io.IOException; // Для обработки исключений ввода-вывода
import java.nio.file.Files; // Для операций с файлами и каталогами
import java.nio.file.Path; // Для работы с путями файлов
import java.nio.file.Paths; // Для создания путей файлов

/**
 * Сервис для сохранения файлов на сервере.
 * Содержит логику для загрузки файлов, включая создание папок и сохранение данных на диск.
 */
@Service // Аннотация, указывающая, что этот класс является сервисом Spring
@RequiredArgsConstructor // Lombok генерирует конструктор, принимающий все поля, помеченные как final или @NonNull
@Slf4j // Добавляет поддержку логгирования с использованием SLF4J
public class FileStorageService { // Класс, представляющий сервис для хранения файлов

    @Value("${application.file.upload.photos-output-path}") // Считывает значение из конфигурации приложения
    private String fileUploadPath; // Путь, куда будут загружаться файлы

    /**
     * Сохраняет файл, связанный с конкретным пользователем.
     * @param sourceFile Загружаемый файл
     * @param userId Идентификатор пользователя
     * @return Путь к сохраненному файлу
     */
    public String saveFile(
            @NonNull MultipartFile sourceFile, // Указывает, что аргумент не может быть null
            @NonNull String userId) { // Идентификатор пользователя, с которым связан файл
        // Создание подкаталога для пользователя
        final String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath); // Загружает файл в соответствующий подкаталог
    }

    /**
     * Загружает файл в указанное место на диске.
     * @param sourceFile Загружаемый файл
     * @param fileUploadSubPath Подкаталог для загрузки файла
     * @return Путь к сохраненному файлу
     */
    private String uploadFile(
            @NonNull MultipartFile sourceFile, // Аргумент, который нельзя сделать null
            @NonNull String fileUploadSubPath) { // Подкаталог, куда загружается файл
        // Полный путь, включая основной путь загрузки и подкаталог
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath); // Объект, представляющий конечную папку

        // Если папка не существует, создаем ее
        if (!targetFolder.exists()) { // Проверяет, существует ли папка
            boolean folderCreated = targetFolder.mkdirs(); // Создает папку, если ее нет
            if (!folderCreated) { // Если папка не создана, выдает предупреждение
                log.warn("Failed to create the target folder");
                return null; // Если не удалось создать папку, возвращает null
            }
        }

        // Получение расширения файла
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename()); // Выделяет расширение файла

        // Формирование уникального пути для сохранения файла
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath); // Путь к конечному файлу

        try {
            Files.write(targetPath, sourceFile.getBytes()); // Сохраняет байты файла в заданное место
            log.info("File saved to " + targetFilePath); // Записывает информацию в журнал
            return targetFilePath; // Возвращает путь к сохраненному файлу
        } catch (IOException e) { // Обработка исключения ввода-вывода
            log.error("File was not saved", e); // Записывает ошибку в журнал
        }

        return finalUploadPath; // Возвращает путь к папке, если файл не сохранен
    }

    /**
     * Получает расширение файла из имени файла.
     * @param fileName Имя файла
     * @return Расширение файла или пустая строка, если расширение отсутствует
     */
    private String getFileExtension(String fileName) { // Метод для получения расширения файла
        if (fileName == null || fileName.isEmpty()) { // Если имя файла пустое или null
            return ""; // Возвращает пустую строку
        }

        int lastDotIndex = fileName.lastIndexOf("."); // Находит последнюю точку в имени файла
        if (lastDotIndex == -1) { // Если точки нет, расширение не найдено
            return ""; // Возвращает пустую строку
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase(); // Возвращает расширение в нижнем регистре
    }

}
