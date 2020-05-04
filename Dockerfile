FROM openjdk:8-jdk-alpine
MAINTAINER GSD
LABEL description="mylibrary-user-service"
RUN ["mkdir", "-p", "/opt/app"]
RUN ["mkdir", "-p", "/opt/app/mylibrary-h2-db"]
WORKDIR /opt/app
COPY ["target/mylibrary-user-service*.jar", "mylibrary-user-service.jar"]
ENTRYPOINT ["java", "-Xmx128m", "-jar", "mylibrary-user-service.jar"]