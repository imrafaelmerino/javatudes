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

mkdir -p $OUTPUT | (rm -r $OUTPUT/* & echo "")

javac -d $OUTPUT \
$SOURCE/types/*.java \
$SOURCE/etudes/*.java \
$SOURCE/search/*.java \
$SOURCE/advent_of_code_2021/Day_15_Chiton/*.java
}

main "$@"