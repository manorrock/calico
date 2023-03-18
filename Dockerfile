FROM piranhacloud/webprofile:v23.3.0
COPY target/calico.war ROOT.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-webprofile.jar", "--war-file", "ROOT.war", "--verbose"]
