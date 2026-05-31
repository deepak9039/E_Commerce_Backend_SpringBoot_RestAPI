FROM eclipse-temurin:21-jdk
COPY target/*.jar e-commerce-app.jar
EXPOSE 1234
ENTRYPOINT ["java", "-jar", "e-commerce-app.jar"]