FROM store/oracle/serverjre:8

WORKDIR /target

COPY /target/eureka-service-0.0.1-SNAPSHOT.jar ./

EXPOSE 8761

CMD ["java", "-jar", "eureka-service-0.0.1-SNAPSHOT.jar"]