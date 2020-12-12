#!/bin/bash

read -p 'Application Main Class Name: ' applicationName
read -p 'Name: ' name

echo "Create Demo application $name"
http https://start.spring.io/starter.tgz \
    dependencies==actuator,web,webflux,security,prometheus,cloud-starter,data-jpa,h2 \
    applicationName=="$applicationName" \
    description=="Demo Spring Boot project with Kotlin" \
    name=="$name" \
    groupId=="ch.keepcalm" \
    artifactId=="$name" \
    packageName=="ch.keepcalm.demo" \
    javaVersion==11 \
    language==kotlin \
    type==gradle-project \
    baseDir=="$name" | tar -xzvf -

echo "Remove application.properties file"
rm $name/src/main/resources/application.properties



echo "Create application.yaml file"
(
cat <<APPCONF
spring:
  profiles:
    active: default
    include: common
  application:
    name:  $name
APPCONF
) > $name/src/main/resources/application.yaml



echo "Create application-common.yaml file"
cat <<\EOT > $name/src/main/resources/application-common.yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      enabled: true
    restart:
      enabled: true
    info:
      enabled: true
  info:
    git:
      mode: full
EOT



echo "Download banner.txt"
http https://raw.githubusercontent.com/marzelwidmer/marzelwidmer.github.io/master/img/banner.txt >  $name/src/main/resources/banner.txt


