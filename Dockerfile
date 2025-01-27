FROM maven:3.9.9-eclipse-temurin-21

ARG APPLICATION_YML
WORKDIR /coflow

COPY . ./
RUN mkdir -p ./src/main/resources
RUN echo "${APPLICATION_YML}" > ./src/main/resources/application.yml

RUN chmod +x gradlew
RUN ./gradlew build -x test

ENTRYPOINT ["java", "-jar", "./build/libs/coflow-0.0.1-SNAPSHOT.jar"]
