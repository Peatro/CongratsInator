# CongratsInator

**CongratsInator** — это Java 21 приложение для ведения списка дней рождения друзей, знакомых и сотрудников. Проект демонстрирует навыки объектно-ориентированного проектирования, работы с базой данных, Spring Boot и веб-интерфейса с Thymeleaf.

Приложение поддерживает следующие функции:

* Отображение всех дней рождения
* Отображение сегодняшних и ближайших дней рождения
* Добавление, редактирование и удаление записей
* Загрузка и отображение фотографий именинников

---

## **Технологии**

* Java 21
* Spring Boot 3
* Spring Data JPA
* PostgreSQL
* Thymeleaf (веб-шаблоны)
* Gradle 9.2.1 с Kotlin DSL

---

## **Установка и запуск**

1. **Склонировать репозиторий**

```bash
git clone https://github.com/<your-username>/CongratsInator.git
cd CongratsInator
```

2. **Настроить базу данных PostgreSQL**

```sql
CREATE DATABASE birthdaydb;
CREATE USER postgres WITH PASSWORD 'yourpassword';
GRANT ALL PRIVILEGES ON DATABASE birthdaydb TO postgres;
```

3. **Настроить `application.yml`**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/birthdaydb
    username: postgres
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

4. **Собрать и запустить проект**

```bash
./gradlew bootRun
```

5. **Открыть веб-интерфейс**

* Перейти в браузере по адресу: `http://localhost:8080`

---

## **Возможности для расширения**

* SPA-клиент на React или Vue через REST API
* Сервис рассылки уведомлений о приближающихся днях рождения
* Расширенная сортировка и фильтры
* Адаптивный и более интерактивный интерфейс

---

## **Контакты**

Проект разработан PeatroXD | Алексеем Бобровым в качестве тестового задания по Java-разработке.

