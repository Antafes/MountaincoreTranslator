#!/bin/sh

ACCESS_TOKEN="$1"
ROOT_FOLDER="$( pwd )/../"
M2_HOME="${HOME}/.m2"
M2_CACHE="${ROOT_FOLDER}/maven"

if [ -d "${M2_CACHE}" ] && [ ! -d "${M2_HOME}" ]
then
    echo "Generating symbolic link for cache"
    ln -s "${M2_CACHE}" "${M2_HOME}"
fi

echo "Write m2 settings"
cat > ${M2_HOME}/settings.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <activeProfiles>
        <activeProfile>github</activeProfile>
    </activeProfiles>
    <profiles>
        <profile>
            <id>github</id>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                    <releases><enabled>true</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </repository>
                <repository>
                    <id>github</id>
                    <name>GitHub Antafes Apache Maven Packages</name>
                    <url>https://maven.pkg.github.com/antafes/MyXML</url>
                </repository>
            </repositories>
        </profile>
        <profile>
            <id>SUREFIRE-1588</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <argLine>-Djdk.net.URLClassPath.disableClassPathURLCheck=true</argLine>
            </properties>
        </profile>
    </profiles>

    <servers>
        <server>
            <id>github</id>
            <username>antafes</username>
            <password>${ACCESS_TOKEN}</password>
        </server>
    </servers>
</settings>
EOF