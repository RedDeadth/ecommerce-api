#!/bin/sh
# Maven Wrapper — uses Java 21
export JAVA_HOME=/usr/lib/jvm/java-1.21.0-openjdk-amd64

MAVEN_HOME="${HOME}/.m2/wrapper/dists/apache-maven-3.9.6"
MAVEN_CMD="${MAVEN_HOME}/bin/mvn"

if [ ! -f "${MAVEN_CMD}" ]; then
    echo "Downloading Maven 3.9.6..."
    mkdir -p "${MAVEN_HOME}"
    curl -sL "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.tar.gz" | tar xz -C "${HOME}/.m2/wrapper/dists/" 2>/dev/null
    EXTRACTED_DIR=$(find "${HOME}/.m2/wrapper/dists/" -name "mvn" -path "*/bin/mvn" 2>/dev/null | head -1)
    if [ -n "${EXTRACTED_DIR}" ]; then
        MAVEN_CMD="${EXTRACTED_DIR}"
    fi
fi

exec "${MAVEN_CMD}" "$@"
