FROM store/oracle/serverjre:8

WORKDIR /target

COPY /target/book-gateway-service-0.0.1-SNAPSHOT.jar ./

EXPOSE 8890

CMD ["java", "-jar", "book-gateway-service-0.0.1-SNAPSHOT.jar"]