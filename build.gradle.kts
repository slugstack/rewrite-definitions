/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    `maven-publish`
    signing // todo
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0" // https://github.com/gradle-nexus/publish-plugin
    id("nebula.release") version "15.2.0"
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
// version = "0.0.14-SNAPSHOT"
description = "rewrite-definitions"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////
// why. WHY is there not an easy way to do 'closeAndPromoteRepository' without more freaking plugins? WHY?!
////////////////////////////////////////////////////////////////

// TODO
java {
    withJavadocJar()
    withSourcesJar()
}


// todo trying out plugin
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
nexusPublishing {
    repositories {
        create("mavenCentral") {
            nexusUrl.set(uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/"))
            snapshotRepositoryUrl.set(uri("https://oss.sonatype.org/content/repositories/snapshots/"))
            username.set("ossrhUsername") // defaults to project.properties["myNexusUsername"]
            password.set("ossrhPassword") // defaults to project.properties["myNexusPassword"]
        }
    }
}

// publishing {
//     repositories {
//         // todo just example
//         // maven {
//         //     name = "GitHubPackages"
//         //     url = uri("https://maven.pkg.github.com/OWNER/REPOSITORY")
//         //     credentials {
//         //         username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
//         //         password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
//         //     }
//         // }

//         maven {
//             name = "OSSRH"
//             val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
//             val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
//             url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
//             credentials {
//                 username = project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME") // alt, mavenCentralRepositoryUsername?
//                 password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD") // alt, mavenCentralRepositoryPassword?
//             }
//         }
//     }

//     publications.create<MavenPublication>("maven") {
//         from(components["java"])
//     }

//     // publications {
//     //     register("gpr") {
//     //         from(components["java"])
//     //     }
//     // }

// }




signing {
    // sign(publishing.publications["mavenJava"])
    // sign(publishing.publications["maven"]) // todo
}
