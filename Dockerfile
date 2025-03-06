FROM maven:3-eclipse-temurin-21-alpine AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml clean package -Dglass.platform=Monocle -Dmonocle.platform=Headless -Dprism.order=sw

FROM eclipse-temurin:21-jre-alpine AS package
RUN apk add xvfb gtk+3.0
ENV DISPLAY :99
ARG JAR_FILE=/usr/app/target/static-imager-jar-with-dependencies.jar
COPY --from=build $JAR_FILE /app/app.jar
ADD run.sh /run.sh
RUN chmod a+x /run.sh
EXPOSE 7070
ENV nasa=DEMO_KEY
CMD /run.sh