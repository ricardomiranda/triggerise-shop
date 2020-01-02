FROM openjdk:8-jre-alpine

LABEL maintainer="mail@ricardoMiranda.com"
LABEL build_date="Jan 2020"
LABEL description="Triggerise shop container"

RUN mkdir -p /opt/app/triggerise-shop && \
    adduser -D -g '' triggerise
USER triggerise

WORKDIR /opt/app
COPY ./src/scripts/run_jar.sh ./target/scala-2.12/triggerise-shop.jar ./src/main/resources/pricing_rules.json ./
USER root
RUN ["chmod", "755", "./run_jar.sh"]
USER triggerise
ENTRYPOINT ["./run_jar.sh"]
