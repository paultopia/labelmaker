FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/labelmaker.jar /labelmaker/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/labelmaker/app.jar"]
