FROM eclipse-temurin:21-alpine
COPY target/static-imager.jar /app.jar
EXPOSE 7070
ENV nasa=DEMO_KEY
ENTRYPOINT java -jar /app.jar --nasa $nasa