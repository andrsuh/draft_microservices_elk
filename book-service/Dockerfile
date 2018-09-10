FROM ubuntu_java

WORKDIR /target

COPY /target/book-service-0.0.1-SNAPSHOT.jar ./

EXPOSE 8888

CMD ["java", "-jar", "book-service-0.0.1-SNAPSHOT.jar"]