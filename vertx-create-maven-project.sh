#!/usr/bin/env bash

export PROJECT_NAME="vertx-starter"
export GROUP_ID="io.vertx.starter"
export ARTIFACT_ID=$PROJECT_NAME
export VERSION="1.0-SNAPSHOT"
export VERTX_PORT="8080"

# Read project name
read -p "What's the name of your project? [$PROJECT_NAME] : " projectName
if [ ${#projectName} -ge 1 ]; then PROJECT_NAME=$projectName;
fi

# Read groupID, Artifact id aind Version
read -p "What's the groupId of your project? [$GROUP_ID] : " groupId
if [ ${#groupId} -ge 1 ]; then GROUP_ID=$groupId;
fi

read -p "What's the artifactId of your project? [$PROJECT_NAME] : " artifactId
if [ ${#artifactId} -ge 1 ]; then ARTIFACT_ID=$artifactId;
else ARTIFACT_ID=${PROJECT_NAME}
fi

read -p "What's the PORT of your Application? [$VERTX_PORT] : " port
if [ ${#port} -ge 1 ]; then VERTX_PORT=$port;
fi

read -p "What's the version of your project? [$VERSION] : " v
if [ ${#v} -ge 1 ]; then VERSION=$v;
fi

echo "Cloning project"
git clone -b v0.1.1.vertx-starter-normal git@github.com:chunhui2001/vertx-maven-starter.git ${PROJECT_NAME}

echo "Generating project"
rm -Rf ${PROJECT_NAME}/.git
sed -i -e "s/io.vertx.starter/${GROUP_ID}/" ${PROJECT_NAME}/pom.xml
sed -i -e "s/vertx-start-project/${ARTIFACT_ID}/" ${PROJECT_NAME}/pom.xml
sed -i -e "s/1.0-SNAPSHOT/${VERSION}/" ${PROJECT_NAME}/pom.xml
sed -i -e "s/main.verticle/vertx.verticle/g" ${PROJECT_NAME}/pom.xml
sed -i -e "s/8080/${VERTX_PORT}/" `find ${PROJECT_NAME} -iname MainVerticle*.java`
sed -i -e "s/io.vertx.starter.MainVerticle/${GROUP_ID}.MainVerticle/" ${PROJECT_NAME}/redeploy.*

echo "Rename package"
mkdir -p ${PROJECT_NAME}/src/main/java/`echo ${GROUP_ID} | sed -e 's/\./\//g'`
mkdir -p ${PROJECT_NAME}/src/test/java/`echo ${GROUP_ID} | sed -e 's/\./\//g'`

rsync -az ${PROJECT_NAME}/src/main/java/io/vertx/starter/* -p ${PROJECT_NAME}/src/main/java/`echo ${GROUP_ID} | sed -e 's/\./\//g'`
rsync -az ${PROJECT_NAME}/src/test/java/io/vertx/starter/* -p ${PROJECT_NAME}/src/test/java/`echo ${GROUP_ID} | sed -e 's/\./\//g'`

sed -i -e "s/io.vertx.starter/${GROUP_ID}/" `find ${PROJECT_NAME} -iname MainVerticle*.java`

rm -rf ${PROJECT_NAME}/src/main/java/io
rm -rf ${PROJECT_NAME}/src/test/java/io
rm -rf `find vertx-starter/ -iname *-e`

echo "======================================"
echo " To check your generated project run:"
echo "     cd $PROJECT_NAME"
echo "     mvn clean test exec:java"
echo " and open your browser to 'http://localhost:${VERTX_PORT}'"
echo " Happy coding !"
echo "======================================"
