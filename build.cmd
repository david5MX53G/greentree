rem This sets up variables for Apache Ant and runs the default target in build.xml using Ant.
rem Ensure there is a valid Ant build.xml file in the same dir as this script before running.
rem See "https://stackoverflow.com/questions/1851012/set-a-path-variable-with-spaces-in-the-path-in-a-windows-cmd-file-or-batch-file"
rem See "Ant/Tutorial/AntTutorial.htm" Mike Prasad posted Jun 23, 2018 9:24 AM MSSE670_X40_Java Software Development
set PATH=
set CLASSPATH=
set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_171"
set "ANT_HOME=C:\Program Files\Java\apache-ant-1.10.4"
set PATH=%JAVA_HOME%\bin;.;%ANT_HOME%\bin
ant
