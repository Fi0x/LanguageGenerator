FROM openjdk:19-jdk-alpine
MAINTAINER fi0x
COPY target/LanguageGenerator-0.0.0.1.jar language-generator-0.0.0.1.jar
ENTRYPOINT ["java","-jar","/language-generator-0.0.0.1.jar"]