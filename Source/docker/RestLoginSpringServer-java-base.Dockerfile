# DO NOT USE THIS 
# BROKEN
FROM openjdk:11

WORKDIR /source/root
ADD root/mvnw ./
RUN ./mvnw install

ADD root/src ./src
RUN ./mvnw install

WORKDIR /source/server
ADD server/rest-login/mvnw ./
ADD server/rest-login/pom.xml ./
RUN ./mvnw install 

ADD server/rest-login/src ./server
RUN ./mvnw install

WORKDIR /source/server

ENTRYPOINT ["./mvnw","spring-boot:run"]