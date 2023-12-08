#!/usr/bin/env bash
set -o errexit
set -o pipefail
#set -o xtrace

echo ""
echo "Executing script:$0 $@"
echo ""

main() {

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

OUTPUT="$DIR/classes"

SOURCE="$DIR/src/main/java"

LIB_DIR="$DIR/libs"

mkdir -p $OUTPUT | (rm -r $OUTPUT/* & echo "")

find $SOURCE/. -name "*.java" -print | xargs javac -cp "$LIB_DIR/*" -d $OUTPUT

}

main "$@"