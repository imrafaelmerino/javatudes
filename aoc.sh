#!/usr/bin/env bash
set -o errexit
set -o pipefail
#set -o xtrace

echo ""
echo "Executing script:$0 $@"
echo ""
usage() {
    echo "Usage: $0 --year <2022|2203> --day <1-24> --times <positive_integer> [-ea]"
    exit 1
}
main() {

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

OUTPUT="$DIR/classes"

LIB_DIR="$DIR/libs"

ENABLE_ASSERTIONS=false  # Default value




# Validate the number of parameters
if [ "$#" -lt 6 ] || [ "$#" -gt 7 ]; then
    usage
fi

# Parse command line arguments
while [ "$#" -gt 0 ]; do
    case "$1" in
        --year)
            year="$2"
            shift 2
            ;;
        --day)
            day="$2"
            shift 2
            ;;
        --times)
            times="$2"
            shift 2
            ;;
        -ea)
            ENABLE_ASSERTIONS=true
            shift
            ;;
        *)
            usage
            ;;
    esac
done

# Validate input values
if [ "$year" != "2022" ] && [ "$year" != "2023" ]; then
    echo "Invalid year. Valid values are 2022 or 2023."
    exit 1
fi

if [ "$day" != "all" ] && ! [[ "$day" =~ ^[1-9]$|^1[0-9]$|^2[0-4]$ ]]; then
    echo "Invalid day. Day must be between 1 and 24."
    exit 1
fi

if ! [[ "$times" =~ ^[1-9][0-9]*$ ]]; then
    echo "Invalid times. Times must be a positive integer."
    exit 1
fi

# Enable assertions if the -ea flag is passed
if [ "$ENABLE_ASSERTIONS" = true ]; then
    JAVA_OPTS="$JAVA_OPTS -ea"
fi


java $JAVA_OPTS -classpath "$OUTPUT:$LIB_DIR/*" advent_of_code._${year}._${year} $day $times

}


main "$@"