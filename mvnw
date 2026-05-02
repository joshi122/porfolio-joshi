#!/bin/sh
if [ -z "$MAVEN_HOME" ] ; then
  MAVEN_HOME="$(cd "$(dirname "$0")" && pwd)/.mvn"
fi
if [ -z "$JAVA_HOME" ] ; then
  JAVA_HOME="$(/usr/libexec/java_home)"
fi
exec "$JAVA_HOME/bin/java" \
  -classpath "$MAVEN_HOME/wrapper/maven-wrapper.jar" \
  "-Dmaven.multiModuleProjectDirectory=" \
  "-Dmaven.home=$MAVEN_HOME" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
