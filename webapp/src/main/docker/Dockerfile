FROM manorrock/openjdk
COPY target/*-all.jar /opt/files_webapp/files_webapp.jar
CMD ["java", "-XX:+ExitOnOutOfMemoryError", "-jar", "/opt/files_webapp/files_webapp.jar"]