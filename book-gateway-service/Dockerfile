FROM ubuntu_java

WORKDIR /target

COPY /target/book-gateway-service-0.0.1-SNAPSHOT.jar ./

EXPOSE 8890

CMD ["java", "-jar", "book-gateway-service-0.0.1-SNAPSHOT.jar"]