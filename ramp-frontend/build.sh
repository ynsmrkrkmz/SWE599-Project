#!/bin/sh
IMAGE_NAME="us-central1-docker.pkg.dev/ramp-423320/ramp/ramp-web"
docker build -f Dockerfile . --tag $IMAGE_NAME --platform linux/amd64 --no-cache
docker push $IMAGE_NAME
