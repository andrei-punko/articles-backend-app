FROM openjdk:17-jdk-alpine
VOLUME /tmp
EXPOSE 8099
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/articles-backend-app-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar"]
