FROM manorrock/openjdk
ENV PATH_PREFIX /files/rest
ENV ROOT_DIRECTORY /mnt
ENV SERVER_PORT 8081
COPY target/*-all.jar /opt/files_rest/files_rest.jar
CMD ["java", "-XX:+ExitOnOutOfMemoryError", "-jar", "/opt/files_rest/files_rest.jar"]
