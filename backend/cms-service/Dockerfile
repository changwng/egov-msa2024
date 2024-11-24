FROM openjdk:11-jdk

WORKDIR /app

COPY build/libs/*.jar app.jar

ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

EXPOSE 8080 5005

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
