#base docker image
FROM openjdk:11
LABEL maintainer="bogdanZdravkovic"
ADD target/releasetracking-0.0.1-SNAPSHOT.jar release-tracking-docker.jar
ENTRYPOINT ["java", "-jar", "release-tracking-docker.jar"]

