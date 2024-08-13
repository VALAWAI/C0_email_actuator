#!/bin/bash
if [ ! -f /.dockerenv ]; then
   echo "You can not start this script if you are not in the development environment"
else
	DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
	pushd $DIR >/dev/null
	if [ ! -f .lastUpdateDate ]; then
		echo "0" > .lastUpdateDate
	fi
	NOW=$(date +'%Y%j');
	LAST_UPDATE=$(cat .lastUpdateDate)
	if [ "$LAST_UPDATE" -lt "$NOW" ]; then
		./mvnw quarkus:update
		echo "$NOW" > .lastUpdateDate
	fi
	popd >/dev/null
fi
