FROM eclipse-temurin:21-alpine
COPY target/static-imager-jar-with-dependencies.jar /app.jar
EXPOSE 7070
ENV nasa=DEMO_KEY
ENTRYPOINT java -jar /app.jar --nasa $nasa