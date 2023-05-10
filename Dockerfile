FROM openjdk:18
WORKDIR /opt/digital-design-final-task
COPY ./application/target/application-1.0-SNAPSHOT-jar-with-dependencies.jar ./
CMD java -jar application-1.0-SNAPSHOT-jar-with-dependencies.jar