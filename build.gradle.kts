//
// heading buffer
//

buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    `java-library` // java // testing
    `maven-publish`
    signing

    id("nebula.maven-resolved-dependencies") version "17.3.2" // testing
    id("nebula.release") version "15.3.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0" // https://github.com/gradle-nexus/publish-plugin

    id("nebula.maven-publish") version "17.3.2"
    id("nebula.contacts") version "5.1.0"
    id("nebula.info") version "9.3.0"

    id("nebula.javadoc-jar") version "17.3.2" // testing
    id("nebula.source-jar") version "17.3.2" // testing
    id("nebula.maven-apache-license") version "17.3.2" // https://plugins.gradle.org/plugin/nebula.maven-apache-license
}

repositories {
    mavenLocal()
    mavenCentral()
    // maven {
    //     url = uri("https://jcenter.bintray.com/")
    // }

    // maven {
    //     url = uri("https://repo.maven.apache.org/maven2/")
    // }
}

dependencies {
    // implementation("org.projectlombok:lombok:1.18.18")
    // testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")


    // ENTIRELY FOR TESTING AN ISSUE::
    compileOnly("org.projectlombok:lombok:latest.release")
    annotationProcessor("org.projectlombok:lombok:latest.release")

    implementation("org.openrewrite:rewrite-java:latest.integration")
    implementation("org.openrewrite:rewrite-xml:latest.integration")
    implementation("org.openrewrite:rewrite-properties:latest.integration")
    implementation("org.openrewrite:rewrite-yaml:latest.integration")
    implementation("org.openrewrite:rewrite-maven:latest.integration")

    // for locating list of released Spring Boot versions
    implementation("com.squareup.okhttp3:okhttp:latest.release")

    runtimeOnly("org.openrewrite.recipe:rewrite-testing-frameworks:latest.integration")

    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.junit.jupiter:junit-jupiter-api:latest.release")
    testImplementation("org.junit.jupiter:junit-jupiter-params:latest.release")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:latest.release")

    testImplementation("org.openrewrite:rewrite-test:latest.integration")

    testImplementation("org.assertj:assertj-core:latest.release")
    testImplementation("com.github.marschall:memoryfilesystem:latest.release")

    // for generating properties migration configurations
    testImplementation("com.fasterxml.jackson.core:jackson-databind:latest.release")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:latest.release")
    testImplementation("io.github.classgraph:classgraph:latest.release")

    testRuntimeOnly("org.openrewrite:rewrite-java-11:latest.integration")
    testRuntimeOnly("org.openrewrite:rewrite-java-8:latest.integration")

    testRuntimeOnly("junit:junit:latest.release")
    testRuntimeOnly("org.springframework:spring-test:4.+")
    testRuntimeOnly("org.springframework:spring-beans:4.+")
    testRuntimeOnly("org.springframework:spring-webmvc:4.+")
    testRuntimeOnly("org.springframework.boot:spring-boot-autoconfigure:1.5.+")
    // END TESTING BLOCK
}

group = "io.slugstack.oss"
description = "slugstack-publishing-test examples"

configure<nebula.plugin.contacts.ContactsExtension> {
    val c = nebula.plugin.contacts.Contact("aegershman@gmail.com")
    c.moniker("Aaron Gershman")
    c.github("aegershman")
    people["aegershman@gmail.com"] = c
}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configurations.all {
    resolutionStrategy {
        cacheChangingModulesFor(0, TimeUnit.SECONDS)
        cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
    }

    // We use kotlin exclusively for tests
    // The kotlin plugin adds kotlin-stdlib dependencies to the main sourceSet, even if it doesn't use any kotlin
    // To avoid shipping dependencies we don't actually need, exclude them from the main sourceSet classpath but add them _back_ in for the test source sets
    if (name == "compileClasspath" || name == "runtimeClasspath") {
        exclude(group = "org.jetbrains.kotlin")
    }
}

configure<nebula.plugin.release.git.base.ReleasePluginExtension> {
    // without this, the default is to use "-dev.x.uncommited+sha" during build ./gradlew build etc., e.g.
    // Inferred project: slugstack-publishing-test, version: 0.1.0-dev.1.uncommitted+ae68a9d
    // but WITH this config, ./gradlew build defaults to: Inferred project: slugstack-publishing-test, version: 0.1.0-SNAPSHOT
    defaultVersionStrategy = nebula.plugin.release.NetflixOssStrategies.SNAPSHOT(project)
}

// java {
//     withJavadocJar()
//     withSourcesJar()
// }

nexusPublishing {
    repositories {
        sonatype {
            username.set(project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME"))
            password.set(project.findProperty("ossrhToken") as String? ?: System.getenv("OSSRH_TOKEN"))
        }
    }
}

signing {
    setRequired({
        // skip signing snapshots by default, unless you explicitly opt-in with -PforceSigning
        !project.version.toString().endsWith("SNAPSHOT") || project.hasProperty("forceSigning")
    })
    val signingKey = project.findProperty("signingKey") as String? ?: System.getenv("SIGNING_KEY")
    val signingPassword = project.findProperty("signingPassword") as String? ?: System.getenv("SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
    // useGpgCmd() // todo for local testing
    sign(publishing.publications["nebula"])
}
