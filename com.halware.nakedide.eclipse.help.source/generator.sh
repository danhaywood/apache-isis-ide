#!/bin/sh

# -----------------------------------------------------------------
#
# Driver script for the DOPUS docbook environment
#
# -----------------------------------------------------------------

# The DOPUS root directory
dopusdir=$("pwd")
export DOPUS_HOME=$dopusdir

SYSTEM_HOME=${DOPUS_HOME}/system
export ANT_HOME=${SYSTEM_HOME}/ant

OLDIFS=$IFS

IFS='
'
for i in $(find "$ANT_HOME/lib" -type f | sed "s/^.*\/\.svn\/.*$//g" | grep ".jar") 
do
 LOCALCLASSPATH="$LOCALCLASSPATH:$i"
done

PARAM_LINE="-Dproject.input.document=$1 -Dproject.output.mode=$2"

if [ "$3" != "" ] 
then
  PARAM_LINE="${PARAM_LINE} -DENV_PARAM_3=$3"
fi

if [ "$JAVA_HOME" = "" ]
then
  echo "Variable JAVA_HOME not set. Try to figure out where it is..."
  JAVA_LOCATION=`which java`
  if [ $JAVA_LOCATION = "" ]
  then
    echo "No Java JRE found. Please install JRE 1.4 or above and set JAVA_HOME. Exiting..."
    exit 1;
  else
    echo "JAVA is at $JAVA_LOCATION"
  fi
else
  JAVA_LOCATION=$JAVA_HOME/bin/java
fi

IFS=$OLDIFS

ANT_TARGET=$2

if [ "$1" = "" ] || [ "$1" == "help" ]
then
  echo "# Usage: generator.bat document-name target"
  echo "# A special case is creating a book or article:"
  echo "#   generator.bat document-name create book    or"
  echo "#   generator.bat document-name create article"
  echo "#"
  echo "#"
  ANT_TARGET=-projecthelp
fi

# to have ant being quiet, add -quiet before %ANT_TARGET%
"${JAVA_LOCATION}" -cp "${LOCALCLASSPATH}" ${PARAM_LINE} org.apache.tools.ant.Main -buildfile "${DOPUS_HOME}/system/etc/build-main.xml" -logger org.apache.tools.ant.NoBannerLogger $ANT_TARGET
