FROM store/oracle/serverjre:8

WORKDIR /target

COPY /target/admin-service-0.0.1-SNAPSHOT.jar .

EXPOSE 10023

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "admin-service-0.0.1-SNAPSHOT.jar"]