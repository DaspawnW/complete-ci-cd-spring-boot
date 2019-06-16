FROM gcr.io/distroless/java:11

COPY target/pricing.jar /app.jar
CMD ["/app.jar"]
