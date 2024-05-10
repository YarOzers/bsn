# Этап сборки Angular-приложения
FROM node:20.11.1 AS build-stage

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем package.json и package-lock.json для установки зависимостей
COPY package*.json ./

# Устанавливаем все необходимые зависимости
RUN npm install

# Копируем весь проект в Docker-контекст
COPY . .

# Запускаем команду сборки Angular-приложения
RUN npm run build --prod

# Этап развертывания с помощью Nginx
FROM nginx:alpine AS deploy-stage

# Копируем файл конфигурации Nginx (если нужен)
COPY nginx.conf /etc/nginx/nginx.conf

# Копируем собранное Angular-приложение в директорию Nginx
COPY --from=build-stage /app/dist/book-network-ui /usr/share/nginx/html

# Открываем порт 80
EXPOSE 80
