#!/usr/bin/env bash

# not intended for direct consumption, just a place to put scratchpad notes

set -e

# export OSSRH_USERNAME="xxx"; export OSSRH_TOKEN="xxx"

case "$1" in
testwork)
    ./gradlew snapshot publish -DossrhUsername="${OSSRH_USERNAME}" -DossrhToken="${OSSRH_TOKEN}" -s --console=plain
    ./gradlew snapshot generatePomFileForMavenJavaPublication -s --console=plain
    ./gradlew -Prelease.disableGitChecks=true -Prelease.useLastTag=true final publishToSonatype closeSonatypeStagingRepository -s --console=plain
    ;;

random-assortment)
    exit 1
    ./gradlew generatePomFileForMavenJavaPublication # https://docs.gradle.org/current/userguide/publishing_maven.html build/publications/$pubName/pom-default.xml.
    ./gradlew publishToMavenLocal && cat ~/.m2/repository/io/slugstack/oss/rewrite-definitions/0.2.0-SNAPSHOT/rewrite-definitions-0.2.0-SNAPSHOT.pom

    ./gradlew snapshot publish -DossrhUsername="${OSSRH_USERNAME}" -DossrhToken="${OSSRH_TOKEN}"
    ./gradlew -Prelease.useLastTag=true snapshot publish
    ./gradlew -Prelease.useLastTag=true snapshot publishToMavenLocal
    ./gradlew -Prelease.disableGitChecks=true -Prelease.useLastTag=true final publish -s --console=plain
    ./gradlew snapshot publishToSonatype -s --console=plain
    ./gradlew -Prelease.disableGitChecks=true -Prelease.useLastTag=true final publishToSonatype -s --console=plain
    ./gradlew -Prelease.disableGitChecks=true -Prelease.useLastTag=true final closeAndReleaseSonatypeStagingRepository -s --console=plain
    ./gradlew -Prelease.disableGitChecks=true -Prelease.useLastTag=true final publishToSonatype closeSonatypeStagingRepository -s --console=plain
    ./gradlew -Prelease.disableGitChecks=true -Prelease.useLastTag=true final publishToSonatype closeAndReleaseSonatypeStagingRepository -s --console=plain
    ;;
*)
    exit 1
    ;;
esac
