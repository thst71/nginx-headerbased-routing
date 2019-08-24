FROM library/openjdk:11.0.4-jre
COPY target/frontend-*.jar /app.jar
RUN groupadd app && useradd -g app app && chown app.app /app.jar && chmod 0744 /app.jar
USER app
CMD java -jar /app.jar