#Image
FROM container-registry.oracle.com/graalvm/jdk:17

#Label it for groupping
LABEL aero.tav.project="tams"

#Expose necessary ports
EXPOSE 8080

#Set args to be able to use them as parameters
ARG ARG_EXECUTABLE_FILE
ARG ARG_EXECUTABLE_JAR_FILE

ARG ARG_EXECUTABLE_PATH
ARG ARG_EXECUTABLE_JAR_PATH

#Export args to ENV variables
ENV EXECUTABLE_FILE=${ARG_EXECUTABLE_FILE}
ENV EXECUTABLE_JAR_FILE=${ARG_EXECUTABLE_JAR_FILE}

ENV EXECUTABLE_PATH=${ARG_EXECUTABLE_PATH}
ENV EXECUTABLE_JAR_PATH=${ARG_EXECUTABLE_JAR_PATH}

ENV RUN_WITH_NATIVE=false
ENV WORK_DIR=/app
ENV NATIVE_OPTS=""
ENV JAVA_OPTS=""

#create workdir
WORKDIR ${WORK_DIR}

#Copy executable to current path
COPY ${EXECUTABLE_PATH} .
COPY ${EXECUTABLE_JAR_PATH} .

#set the ownership
RUN chmod +x ${EXECUTABLE_FILE}
RUN pwd
RUN ls -al

ENTRYPOINT if [ "$RUN_WITH_NATIVE" = "true" ]; then \
                echo "Running app: ${WORK_DIR}/${EXECUTABLE_FILE} as native executable with opts: ${NATIVE_OPTS}" && \
                exec "${WORK_DIR}/${EXECUTABLE_FILE}" ${NATIVE_OPTS}; \
            else \
                echo "Running app: ${EXECUTABLE_JAR_FILE} as JVM mode with opts ${JAVA_OPTS}" && \
                exec java ${JAVA_OPTS} -jar ${EXECUTABLE_JAR_FILE}; \
            fi
