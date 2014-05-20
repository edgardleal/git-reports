#!/bin/bash

declare -r LIB="lib"
declare -r TARGET="target"

if [ ! "$LIB" ]; then 
  mkdir "$LIB"
fi

mvn package

cp "$TARGET/git*.jar" "$LIB/"
