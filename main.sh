#!/usr/bin/env bash

# not intended for direct consumption, just a place to put scratchpad notes

set -e

# export OSSRH_USERNAME="xxx"; export OSSRH_TOKEN="xxx"

case "$1" in
testwork)
    ./gradlew snapshot publish -DossrhUsername="${OSSRH_USERNAME}" -DossrhToken="${OSSRH_TOKEN}"
    ;;

random-assortment)
    exit 1
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
