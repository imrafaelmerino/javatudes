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

java -classpath $OUTPUT $1

}


main "$@"