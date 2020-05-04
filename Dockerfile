FROM openjdk:8-jdk-alpine
MAINTAINER GSD
LABEL description="mylibrary-user-service"
RUN ["mkdir", "-p", "/opt/app"]
WORKDIR /opt/app
COPY ["target/mylibrary-user-service*.jar", "mylibrary-user-service.jar"]
ENTRYPOINT ["java", "-Xmx256m", "-jar", "mylibrary-user-service.jar"]