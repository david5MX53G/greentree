#!/bin/bash
# This script runs the default target of build.xml using Apache Ant.
# Ensure there is an Ant build.xml file with a valid default target in the pwd of this script.
export JAVA_HOME=/opt/jdk1.8.0_171
export ANT_HOME=/opt/apache-ant-1.10.4
export PATH=$PATH:$JAVA_HOME/bin:$ANT_HOME/bin
ant
