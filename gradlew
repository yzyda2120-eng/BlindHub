#!/usr/bin/env sh

##############################################################################
#
# Gradle build script for Linux/OS X
#
##############################################################################

# Determine the Java command to use to start the JVM.
if [ -n """$JAVA_HOME""" ] ; then
    if [ -x """$JAVA_HOME/jre/sh/java""" ] ; then
        # IBM
        JAVACMD="""$JAVA_HOME/jre/sh/java"""
    elif [ -x """$JAVA_HOME/bin/java""" ] ; then
        JAVACMD="""$JAVA_HOME/bin/java"""
    fi
fi

if [ ! -n """$JAVACMD""" ] ; then
    JAVACMD=`which java`
fi

if [ ! -n """$JAVACMD""" ] ; then
    echo "Error: JAVA_HOME is not defined correctly."
    echo "  We cannot execute java"
    exit 1
fi

DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

APP_HOME=`dirname """$0"""`

APP_NAME="Gradle"
APP_BASE_NAME=`basename """$0"""`

# Add default JVM options here. You may also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS"

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

exec """$JAVACMD""" $DEFAULT_JVM_OPTS -classpath """$CLASSPATH""" org.gradle.wrapper.GradleWrapperMain """$@"""
