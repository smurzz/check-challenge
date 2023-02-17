#### Stage 1: Build the application
FROM openjdk:17 as builder

# Set the current working directory inside the image
WORKDIR /app

# Copy the project source
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Copy the project source
COPY src src

# Package the application
ENV MAVEN_OPT="-Xmx1024m -XX:MaxPermSize=256m"
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM openjdk:17

#ENV JAR_FILE=target/*.jar
#
#COPY --from=builder /app/$JAR_FILE /opt/app/
#RUN mv /opt/app/*.jar /opt/app/app.jar

ARG DEPENDENCY=/app/target/dependency

# Copy project dependencies from the build stage
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

#WORKDIR /opt/app
#
#RUN chgrp -R 0 /opt/app && \
#    chmod -R g=u /opt/app
#
#ENV PORT 8080
#
#EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-cp","app:app/lib/*","com.example.checkchallenge.CheckChallengeApplication"]
# "-cp","app:app/lib/*","com.example.checkchallenge.CheckChallengeApplication"