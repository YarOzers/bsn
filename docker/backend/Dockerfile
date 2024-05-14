# Этап сборки (build stage) - на этом этапе мы используем образ Maven для сборки Java-приложения
# Используем образ Maven с OpenJDK 18
FROM maven:3.8.7-openjdk-18 AS build
# Устанавливаем рабочую директорию для всех последующих команд
WORKDIR /build
# Копируем файл pom.xml (Maven-файл конфигурации) в рабочую директорию
COPY pom.xml .
# Загружаем все зависимости, чтобы работать автономно
RUN mvn dependency:go-offline
# Копируем исходный код в папку 'src'
COPY src ./src
# Собираем проект, пропуская тесты, чтобы ускорить сборку
RUN mvn clean package -DskipTests

# Этап исполнения (runtime stage) - здесь мы создаем конечный Docker-образ с нашим Java-приложением
# Используем образ Amazon Corretto (дистрибутив OpenJDK) версии 17
FROM amazoncorretto:17
# Объявляем аргумент 'PROFILE', по умолчанию - 'dev'
ARG PROFILE=dev
# Объявляем аргумент 'APP_VERSION', по умолчанию - '1.0.0'
ARG APP_VERSION=1.0.0
# Устанавливаем рабочую директорию для всех последующих команд в этом этапе
WORKDIR /app
# Копируем собранный JAR-файл из предыдущего этапа (build) в текущую рабочую директорию
COPY --from=build /build/target/book-network-*.jar /app/
# Открываем порт 9090, который приложение будет использовать для прослушивания запросов
EXPOSE 9090

# Устанавливаем переменные окружения, необходимые для запуска приложения
# Адрес базы данных
ENV DB_URL=jdbc:postgresql://postgres-sql-bsn:5432/book_social_network
# Профиль приложения (определяет конфигурацию Spring)
ENV ACTIVE_PROFILE=${PROFILE}
# Версия JAR-файла, который будет запущен
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOST_NAME=missing_host_name
ENV EMAIL_USER_NAME=missing_user_name
ENV EMAIL_PASSWORD=missing_password


# Устанавливаем команду по умолчанию для запуска контейнера
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} book-network-${JAR_VERSION}.jar

# Комадна для сборки образа
# docker build -t bsn/bsn:1.0.0 -f ../docker/backend/Dockerfile .

