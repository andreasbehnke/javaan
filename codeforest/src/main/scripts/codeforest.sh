#!/bin/bash
DIR="$(dirname "$(readlink -f "$0")")"
ARCH="$(uname -m)"
NATIVE_LIB_DIR="i386"
if ([ $ARCH = "x86_64" ] || [ $ARCH = "amd64" ]); then
  NATIVE_LIB_DIR="amd64"
fi
export LD_LIBRARY_PATH=$DIR/$NATIVE_LIB_DIR
java -jar $DIR/codeforest.jar
