FROM openjdk:11
EXPOSE 8080
ADD target/product-service-dev.jar product-service-dev.jar
ENTRYPOINT ["java", "-jar", "product-service-dev.jar"]