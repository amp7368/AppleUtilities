ARG GRADLE_VERSION
FROM ${GRADLE_VERSION} as buildstage

ARG WORKINGDIR
WORKDIR ${WORKINGDIR}

COPY ./src ./src
COPY ./gradle ./gradle
COPY ./gradle.properties .
COPY ./gradlew* .
COPY ./settings.gradle .
COPY ./build.gradle .

RUN ./gradlew -q docker
ENTRYPOINT ["tail", "-f", "/dev/null"]
