#!/bin/sh

ACCESS_TOKEN="$1"
ROOT_FOLDER="$( pwd )/../"
M2_HOME="${HOME}/.m2"
M2_CACHE="${ROOT_FOLDER}/maven"
CI="$( pwd )/ci"

echo "Generating symbolic link for cache"

if [ -d "${M2_CACHE}" ] && [ ! -d "${M2_HOME}" ]
then
    ln -s "${M2_CACHE}" "${M2_HOME}"
fi

VERSION=`cat VERSION`

apt update
apt install zip

# Setup maven settings
$( pwd )/ci/set-m2-settings.sh ${ACCESS_TOKEN}

# Start build without tests
mvn -Dmaven.test.skip=true clean package

# Package windows executable
ZIPFILE=`basename *.zip`
unzip ${ZIPFILE}
cp "${CI}/MountaincoreTranslator.bat" "./MountaincoreTranslator-${VERSION}/"

wget -O openjdk-windows-x64_bin.zip https://drive.google.com/file/d/1JO5FY7dnJ5zuYSDffNU42cxQbqXt5iQQ/view?usp=share_link
unzip -d ./ "./openjdk-windows-x64_bin.zip"
cp -r ./jdk-19.0.2/jre ./MountaincoreTranslator-${VERSION}/

zip -u -r MountaincoreTranslator-${VERSION}-win.zip ./MountaincoreTranslator-${VERSION}/

# Move created files to output
cp target/*.zip ../dist

echo "v$VERSION" >> ../dist/name
echo "v$VERSION" >> ../dist/tag
