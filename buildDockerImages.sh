#!/bin/bash
if ! docker stats --no-stream >/dev/null 2>&1; then
    echo "Docker does not seem to be running, run it first and retry"
    exit 1
else
	DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
	pushd $DIR > /dev/null
	VERSION=$(grep --max-count=1 '<version>' pom.xml | awk -F '>' '{ print $2 }' | awk -F '<' '{ print $1 }')
	FILE_DATE=$(date -r src/dev/docker/Dockerfile +%s)
	IMAGE_DATE=$(docker inspect --format='{{.Created}}' valawai/c0_email_actuator:dev 2>/dev/null)
	if [ $? -ne 0 ]; then
		IMAGE_DATE=0
	else
		IMAGE_DATE=$(echo $IMAGE_DATE|date +%s)
	fi
	if [ $FILE_DATE -ge $IMAGE_DATE ]; then
		DOCKER_BUILDKIT=1 docker build -f src/dev/docker/Dockerfile -t valawai/c0_email_actuator:dev .
	fi
	TAG=${1:-$VERSION}
	DOCKER_PARAMS="--rm --name c0_email_actuator_build_docker_image --add-host=host.docker.internal:host-gateway -v /var/run/docker.sock:/var/run/docker.sock"
	if [[ "$OSTYPE" == "darwin"* ]]; then
		DOCKER_PARAMS="$DOCKER_PARAMS -e TESTCONTAINERS_HOST_OVERRIDE=docker.for.mac.host.internal"
	fi
	docker run $DOCKER_PARAMS -v "${HOME}/.m2":/root/.m2  -v "${PWD}":/app valawai/c0_email_actuator:dev ./mvnw clean package -DskipTests -Dquarkus.container-image.tag=$TAG
	if [ $? -ne 0 ]; then
		echo "Cannot build docker image"
		popd > /dev/null
		exit 1
	else
		popd > /dev/null
		exit 0
	fi
fi
