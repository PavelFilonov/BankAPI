# Руководство по запуску

## Технологии и зависимости
* Java 21+
* Git
* Maven 3.8+
* Docker
* Docker-compose

## Подготовка к запуску
* Установить зависимости
``` shell
mvn clean install
```
* Запустить контейнеры с зависимостями
``` shell
docker-compose up -d
```

## Запуск из IDE
```
Запустить BankApiApplication.java
```

## Сборка и запуск контейнера приложения
``` shell
docker-compose-backend up -d
```