FROM maven

WORKDIR /source/root
ADD root/pom.xml ./
RUN mvn install 

WORKDIR /source/server
ADD server/rest-login/pom.xml ./
RUN mvn install 

# ^ just download dependencies 
# do this in the early stages 
# so it is not done every time you change the src (long downloads)
# ===========================================

WORKDIR /source/root
ADD root/src ./src
RUN mvn install 

WORKDIR /source/server
ADD server/rest-login/src ./src
RUN mvn install 

# ^ copy sources and build apps 
# ===========================================

WORKDIR /source/server

ENTRYPOINT ["mvn","spring-boot:run"]