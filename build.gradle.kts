//import com.github.jk1.license.LicenseReportExtension
//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import nebula.plugin.contacts.Contact
import nebula.plugin.contacts.ContactsExtension
import java.time.Duration

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        mavenLocal()
    }

    // dependencies {
    //     classpath("org.openrewrite:gradle-openrewrite-project-plugin:latest.integration")
    // }
}

plugins {
    `java-library`
    `maven-publish`
    signing

//    id("nebula.netflixoss") version "9.2.2"

//    id("nebula.oss-publishing") version "1.1.3" // https://plugins.gradle.org/plugin/nebula.oss-publishing
    id("nebula.project") version "8.0.0" // https://github.com/nebula-plugins/nebula-project-plugin
    id("nebula.release") version "15.3.1" apply true // toggle since we're testing moving content to publishing-plugin

    id("nebula.maven-publish") version "17.3.2"
//    id("nebula.netflixoss") version "9.2.2"

//     id("org.openrewrite.project-defaults") version "0.1.0-SNAPSHOT" // from local
//     id("io.slugstack.publishing-plugin") version "0.0.2" // releases
//     id("io.slugstack.publishing-plugin") version "0.1.0-SNAPSHOT" // snapshots from maven central; ./gradlew build --refresh-dependencies
//     id("org.openrewrite.publishing-plugin") version "0.1.0-SNAPSHOT"

    id("nebula.maven-resolved-dependencies") version "17.3.2"
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"

//    id("com.github.hierynomus.license") version "0.15.0" apply false
//    id("org.jetbrains.kotlin.jvm") version "1.4.21"
//    id("com.github.jk1.dependency-license-report") version "1.16"

//     id("nebula.contacts") version "5.1.0"
//     id("nebula.info") version "9.3.0"

//     id("nebula.javadoc-jar") version "17.3.2"
//     id("nebula.source-jar") version "17.3.2"
//    id("nebula.maven-apache-license") version "17.3.2"
}

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_11
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_11
}

group = "io.slugstack.oss"
description = "slugstack-publishing-test examples"

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    mavenLocal()
}

nexusPublishing {
    repositories {
        sonatype()
    }
    connectTimeout.set(Duration.ofMinutes(3))
    clientTimeout.set(Duration.ofMinutes(3))
}

//signing {
//    setRequired({
//        // skip signing snapshots by default, unless you explicitly opt-in with -PforceSigning
//        !project.version.toString().endsWith("SNAPSHOT") || project.hasProperty("forceSigning")
//    })
//    val signingKey = project.findProperty("signingKey") as String? ?: System.getenv("SIGNING_KEY")
//    val signingPassword = project.findProperty("signingPassword") as String? ?: System.getenv("SIGNING_PASSWORD")
//    useInMemoryPgpKeys(signingKey, signingPassword)
//    // useGpgCmd() // todo for local testing
//    sign(publishing.publications["nebula"])
//}

configurations.all {
    resolutionStrategy {
        cacheChangingModulesFor(0, TimeUnit.SECONDS)
        cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
    }
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:latest.release")
    compileOnly("org.projectlombok:lombok:latest.release")
    implementation("com.squareup.okhttp3:okhttp:latest.release")

    testImplementation("org.junit.jupiter:junit-jupiter-api:latest.release")
    testImplementation("org.junit.jupiter:junit-jupiter-params:latest.release")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:latest.release")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    jvmArgs = listOf("-XX:+UnlockDiagnosticVMOptions", "-XX:+ShowHiddenFrames")
}

tasks.named<JavaCompile>("compileJava") {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()

    options.isFork = true
    options.forkOptions.executable = "javac"
    options.compilerArgs.addAll(listOf("--release", "8"))
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

configure<ContactsExtension> {
    val c = Contact("aegershman@gmail.com")
    c.moniker("Aaron Gershman")
    c.github("aegershman")
    people["aegershman@gmail.com"] = c
}


// configure<LicenseExtension> {
//     ext.set("year", Calendar.getInstance().get(Calendar.YEAR))
//     skipExistingHeaders = true
//     header = project.rootProject.file("gradle/licenseHeader.txt")
//     mapping(mapOf("kt" to "SLASHSTAR_STYLE", "java" to "SLASHSTAR_STYLE"))
//     // exclude JavaTemplate shims from license check
//     exclude("src/main/resources/META-INF/rewrite/*.java")
//     strictCheck = true
// }

// configure<LicenseReportExtension> {
//     renderers = arrayOf(com.github.jk1.license.render.CsvReportRenderer())
// }

// configure<PublishingExtension> {
//     publications {
//         named("nebula", MavenPublication::class.java) {
//             suppressPomMetadataWarningsFor("runtimeElements")

//             pom.withXml {
//                 (asElement().getElementsByTagName("dependencies").item(0) as org.w3c.dom.Element).let { dependencies ->
//                     dependencies.getElementsByTagName("dependency").let { dependencyList ->
//                         var i = 0
//                         var length = dependencyList.length
//                         while (i < length) {
//                             (dependencyList.item(i) as org.w3c.dom.Element).let { dependency ->
//                                 if ((dependency.getElementsByTagName("scope")
//                                         .item(0) as org.w3c.dom.Element).textContent == "provided"
//                                     || (dependency.getElementsByTagName("groupId")
//                                         .item(0) as org.w3c.dom.Element).textContent == "org.jetbrains.kotlin"
//                                 ) {
//                                     dependencies.removeChild(dependency)
//                                     i--
//                                     length--
//                                 }
//                             }
//                             i++
//                         }
//                     }
//                 }
//             }
//         }
//     }
// }
//
// TODO not necessary if we're using the upstream plugin, hooray
configure<nebula.plugin.release.git.base.ReleasePluginExtension> {
    // without this, the default is to use "-dev.x.uncommited+sha" during build ./gradlew build etc., e.g.
    // Inferred project: slugstack-publishing-test, version: 0.1.0-dev.1.uncommitted+ae68a9d
    // but WITH this config, ./gradlew build defaults to: Inferred project: slugstack-publishing-test, version: 0.1.0-SNAPSHOT
    defaultVersionStrategy = nebula.plugin.release.NetflixOssStrategies.SNAPSHOT(project)
}
