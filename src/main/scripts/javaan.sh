#!/bin/bash
DIR="$(dirname "$(readlink -f "$0")")"
java -jar $DIR/lib/javaan.jar "$@"
