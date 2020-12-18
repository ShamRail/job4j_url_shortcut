## jobj4_url_shortcut ![img](https://travis-ci.com/ShamRail/job4j_url_shortcut.svg?branch=master)

### Project description

Project represent basic url shortcut service. User register his site and convert his own links 
using this service and paste it anywhere. Redirecting goes through service which count link clicks. 
  
### User technologies

* Java 14
* Spring Boot 2
* Spring Security & JWT authorization
* Spring Data JPA
* PostgreSQL
* Liquibase 

### Requirements to running

* At least Java 14
* PostgreSQL Database
* Created database

### Run project

#### Using Spring Boot CLI

````
mvn spring-boot:run -Dspring-boot.run.arguments=--db=your_database,--user=your_user,--password=your_password,--port=your_port
````

<b>OR</b> using default properties(db=shortcut, user=postgres, password=password, port=8080)

````
mvn spring-boot:run
````

#### Using Java CLI

Compile to jar.

````
mvn install
````

With your settings:

````
java -jar target/shortcut-0.0.1-SNAPSHOT.jar --db=shortcut --user=postgres --password=password --port=your_port
````

<b>OR</b> default settings (db=shortcut, user=postgres, password=password, port=8080)

````
java -jar target/shortcut-0.0.1-SNAPSHOT.jar
````

### Using REST API

#### Register site

````
curl --location --request POST 'http://localhost:8080/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "site": "vk.com"
}'
````

#### Getting token

````
curl --location --request POST 'http://localhost:8080/login' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "your_login",
    "password": "your_password"
}'
````

#### Convert link

````
curl --location --request POST 'http://localhost:8080/convert' \
--header 'Authorization: Bearer your_token\
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "http://job4j.ru:8888/TrackStudio/staticframeset.html#253134"
}'
````

#### Getting statistic

````
curl --location --request GET 'http://localhost:8080/statistic' \
--header 'Authorization: Bearer your_token\
````