FROM store/oracle/serverjre:8

WORKDIR /target

COPY /target/book-service-0.0.1-SNAPSHOT.jar ./

EXPOSE 8888

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "book-service-0.0.1-SNAPSHOT.jar"]