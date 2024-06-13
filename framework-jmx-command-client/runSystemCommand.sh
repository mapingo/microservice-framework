#!/usr/bin/env bash

################################################
# Usage:
#
# To run a command (e.g. CATCHUP):
#   ./runSystemCommand.sh <command name>
#
# To list all commands:
#   ./runSystemCommand.sh
#
################################################

FRAMEWORK_JMX_COMMAND_CLIENT_VERSION=17.3.2-SNAPSHOT
CONTEXT_NAME="people"
USER_NAME="admin"
PASSWORD="admin"

#fail script on error
set -e

echo
echo "Framework System Command Client for $CONTEXT_NAME context"
echo

if [ -z "$1" ]; then
  echo "Listing commands"
  echo
  java -jar target/framework-jmx-command-client-${FRAMEWORK_JMX_COMMAND_CLIENT_VERSION}.jar -l -u "$USER_NAME" -pw "$PASSWORD" -cn "$CONTEXT_NAME"
elif [ "$1" == "--help" ]; then
  java -jar target/framework-jmx-command-client-${FRAMEWORK_JMX_COMMAND_CLIENT_VERSION}.jar --help -u "$USER_NAME" -pw "$PASSWORD" -cn "$CONTEXT_NAME"
fi

COMMAND=$1

if [ ! -z "$2" ]; then
  COMMAND_RUNTIME_ID=$2
  echo "Running command '$COMMAND' with command runtime id '$COMMAND_RUNTIME_ID'"
  echo
  java -jar target/framework-jmx-command-client-${FRAMEWORK_JMX_COMMAND_CLIENT_VERSION}.jar -c "$COMMAND" -crid "$COMMAND_RUNTIME_ID" -u "$USER_NAME" -pw "$PASSWORD" -cn "$CONTEXT_NAME"
else
  echo "Running command '$COMMAND'"
  echo
  java -jar target/framework-jmx-command-client-${FRAMEWORK_JMX_COMMAND_CLIENT_VERSION}.jar -c "$COMMAND" -u "$USER_NAME" -pw "$PASSWORD" -cn "$CONTEXT_NAME"
fi
