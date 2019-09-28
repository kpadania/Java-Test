# Spring Boot Java Test
This project exposes a below API resources for adding,deleting,updating, displaying cities and finding distances between given cities in Kilometers

## Description
This Project shows the list of Cities which are stored In-Memory. Using the following endpoints, different operations can be achieved:
- `/city` : GET - This returns the list of Cities
- `/city/{id}` : GET - This returns the city passed in the id parameter by the user
- `/city/` : POST - Add new city using the City model eg . {"name": "Tokyo","latitude": 35.685,"longitude": 139.751389}
- `/city/{id}` : PUT - Update the city of the given id
- `/city/{id}` : DELETE - Delete the city of the given id
- `/city/findDistance/{id1}/{id2}` : GET - Find the distance between two cities given in the URL parametes id1 and id2 in kilometers

More information about the api an be refered from the swagger documentation provided in the below URL
http://localhost:8080/swagger-ui.html

## Libraries used
- Spring Boot
- Spring Configuration
- Spring REST Controller
- Rest Assured: for Unit testing the REST API
- Swagger
- Development Tools

## Compilation Command
- `mvn clean install` - Plain maven clean and install

## Spring Boot as Docker container
1. This REST API can also be used as a docker container by creating a docker file
2. Create a `Dockerfile` for creating a docker image from the Spring Boot Application
`FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/java-test-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} java-test.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/java-test.jar"]`

3. Using the Dockerfile create the Docker image.
From the directory of Dockerfile - `docker build . -t java-test`

4. Run the Docker image (java-test) created in #3.
` docker run -p 8080:8080 -t java-test`