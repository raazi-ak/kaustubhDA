FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17-temurin
COPY --from=build /app/target/realestate-portal.war /usr/local/tomcat/webapps/
COPY start-tomcat.sh /usr/local/bin/start-tomcat.sh
RUN chmod +x /usr/local/bin/start-tomcat.sh

EXPOSE 8080
CMD ["/usr/local/bin/start-tomcat.sh"]
