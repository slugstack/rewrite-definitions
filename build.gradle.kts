//
// heading buffer
//

plugins {
    java
    `maven-publish`
    signing

    id("nebula.contacts") version "5.1.0"
    id("nebula.info") version "9.3.0"
    id("nebula.maven-apache-license") version "17.3.2" // https://plugins.gradle.org/plugin/nebula.maven-apache-license
    id("nebula.maven-publish") version "17.3.2"
    id("nebula.release") version "15.3.1"

    id("io.github.gradle-nexus.publish-plugin") version "1.0.0" // https://github.com/gradle-nexus/publish-plugin
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://jcenter.bintray.com/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.18")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
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

configure<nebula.plugin.release.git.base.ReleasePluginExtension> {
    // without this, the default is to use "-dev.x.uncommited+sha" during build ./gradlew build etc., e.g.
    // Inferred project: slugstack-publishing-test, version: 0.1.0-dev.1.uncommitted+ae68a9d
    // but WITH this config, ./gradlew build defaults to: Inferred project: slugstack-publishing-test, version: 0.1.0-SNAPSHOT
    defaultVersionStrategy = nebula.plugin.release.NetflixOssStrategies.SNAPSHOT(project)
}

java {
    withJavadocJar()
    withSourcesJar()
}

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
