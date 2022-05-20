#!/usr/bin/env bash

CONTEXT_NAME=framework
FRAMEWORK_VERSION=11.0.0-M13-SNAPSHOT

#fail script on error
set -e


function runSystemLiquibase() {
    echo "Running system Liquibase"
    java -jar target/framework-system-liquibase-${FRAMEWORK_VERSION}.jar --url=jdbc:postgresql://localhost:5432/${CONTEXT_NAME}system --username=${CONTEXT_NAME} --password=${CONTEXT_NAME} --logLevel=info update
    echo "Finished running system liquibase"
}

runSystemLiquibase