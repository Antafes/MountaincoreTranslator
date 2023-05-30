#!/bin/sh

ACCESS_TOKEN="$1"
ROOT_FOLDER="$( pwd )/../"
M2_HOME="${HOME}/.m2"
M2_CACHE="${ROOT_FOLDER}/maven"
CI="$( pwd )/ci"
OPENJDK_FILEID="1JO5FY7dnJ5zuYSDffNU42cxQbqXt5iQQ"

echo "Generating symbolic link for cache"

if [ -d "${M2_CACHE}" ] && [ ! -d "${M2_HOME}" ]
then
    ln -s "${M2_CACHE}" "${M2_HOME}"
fi

VERSION=`cat VERSION`

apt-get update
apt-get install -y zip wget python3-pip
pip install gdown
export PATH="$PATH:~/.local/bin/tqdm:~/.local/bin/gdown"

# Setup maven settings
$( pwd )/ci/set-m2-settings.sh ${ACCESS_TOKEN}

# Start build without tests
mvn -Dmaven.test.skip=true clean package

# Package windows executable
cd target/
ZIPFILE=`basename *.zip`
unzip ${ZIPFILE}
cp "${CI}/MountaincoreTranslator.bat" "./MountaincoreTranslator-${VERSION}/"

gdown -O openjdk-windows-x64_bin.zip "${OPENJDK_FILEID}"
unzip -d ./ "./openjdk-windows-x64_bin.zip"
rm "./openjdk-windows-x64_bin.zip"
cp -r ./jdk-19.0.2/jre ./MountaincoreTranslator-${VERSION}/

zip -u -r MountaincoreTranslator-${VERSION}-win.zip ./MountaincoreTranslator-${VERSION}/

cd ../

# Move created files to output
cp target/*.zip ../dist

echo "v$VERSION" >> ../dist/name
echo "v$VERSION" >> ../dist/tag
