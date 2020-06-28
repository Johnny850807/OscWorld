sudo DOCKER_BUILDKIT=1 docker build . -t maven-compile -f Dockerfile-compile
sudo docker run --rm  -v $(pwd):/usr/src/app  -v ~/.m2:/root/.m2 -v $(pwd)/target:/usr/src/app/target maven-compile
