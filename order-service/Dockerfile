FROM ubuntu_java

WORKDIR /target

COPY /target/order-service-0.0.1-SNAPSHOT.jar .

EXPOSE 9999

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "order-service-0.0.1-SNAPSHOT.jar"]