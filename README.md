# Curso de Spring Avanzado

## Instalación

- [JDK](https://www.oracle.com/java/technologies/downloads/)
- [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2026-06/R/eclipse-jee-2026-06-R-win32-x86_64.zip)
  - Help > Eclipse Marketplace ... > [Spring Tools (aka Spring Tool Suite) 5.2.0](https://marketplace.eclipse.org/content/spring-tools-aka-spring-tool-suite)
  - [Project Lombok](https://projectlombok.org/downloads/lombok.jar)
    - **Instalación:** javaw -jar lombok.jar
- Entorno (opcionales)
  - [Git](https://git-scm.com/)
  - [Maven](https://maven.apache.org/download.cgi)
- Clientes de bases de datos (opcionales)
  - [HeidiSQL](https://www.heidisql.com/download.php)
  - [MongoDB Compass](https://www.mongodb.com/try/download/compass)

## Paquetes Java (descargar y descomprimir)

- <https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-java-5.1.49.zip>
- <https://www.eclipse.org/downloads/download.php?file=/rt/eclipselink/releases/4.0.4/eclipselink-4.0.4.v20240715-059428cdd2.zip>

## Base de datos de ejemplos

- [Página principal Sakila](https://dev.mysql.com/doc/sakila/en/)
- [Diagrama de la BD Sakila](https://raw.githubusercontent.com/jmagit/jmagit/refs/heads/main/sakila-structure.png)

## Instalación para usar contenedores

### Subsistema de Windows para Linux (admin)

- [WSL 2 feature on Windows](https://learn.microsoft.com/es-es/windows/wsl/install)

#### Configuración de puertos dinámicos en Windows (admin)

```bash
netsh int ipv4 set dynamic tcp start=51000 num=14536
```

### Alternativas para el gestor contenedores (usar solo una)

- [Docker Desktop](https://www.docker.com/get-started/)
- [Podman](https://podman.io/docs/installation)
- [Rancher Desktop](https://rancherdesktop.io/)

### Bases de datos

#### MySQL

    docker run -d --name mysql-sakila -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 jamarton/mysql-sakila

#### MongoDB

    docker run -d --name mongodb -p 27017:27017 jamarton/mongodb-contactos

#### Redis

    docker run -d --name redis -p 6379:6379 -p 6380:8001 redis/redis-stack:latest

### Agentes de Mensajería

#### Apache ActiveMQ o Artemis (JMS)

    docker run -d --name activemq -p 1883:1883 -p 5672:5672 -p 8161:8161 -p 61613:61613 -p 61614:61614 -p 61616:61616 apache/activemq-classic

    docker run -d --name artemis -p 1883:1883 -p 5445:5445 -p 5672:5672 -p 8161:8161 -p 9404:9404 -p 61613:61613 -p 61616:61616 apache/activemq-artemis

### Pruebas

#### Servidor de correo electrónico

    docker run -d --name mailhog -p 1025:1025 -p 8025:8025 mailhog/mailhog

## Documentación

- <https://docs.spring.io/spring-framework/reference/>
- <https://docs.spring.io/spring-boot/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/commons/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/jpa/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/redis/docs/current/reference/html/>
- <https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#spring-web>
- <https://docs.spring.io/spring-data/rest/docs/current/reference/html/>
- <https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-loadbalancer>
- <https://docs.spring.io/spring-cloud-config/docs/current/reference/html/>
- <https://docs.spring.io/spring-security/reference/index.html>

## Ejemplos

- <https://github.com/rabbitmq/rabbitmq-tutorials/tree/main/spring-amqp>
- <https://github.com/spring-projects/spring-amqp-samples>
- <https://github.com/spring-projects/spring-kafka/tree/main/samples>
- <https://github.com/spring-projects/spring-petclinic>
- <https://github.com/spring-projects/spring-data-examples>
- <https://github.com/spring-projects/spring-data-rest-webmvc>
- <https://github.com/spring-projects/spring-hateoas-examples>

## Laboratorios

- [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot)
- [Scheduling Tasks](https://spring.io/guides/gs/scheduling-tasks)
- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa)
- [Validating Form Input](https://spring.io/guides/gs/validating-form-input)
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service)
- [Consuming a RESTful Web Service](https://spring.io/guides/gs/consuming-rest)
- [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service)
- [Messaging with JMS](https://spring.io/guides/gs/messaging-jms)
- [Testing the Web Layer](https://spring.io/guides/gs/testing-web)
