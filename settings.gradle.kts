/*
 * This file was generated by the Gradle 'init' task.
 */

rootProject.name = "slugstack-publishing-test"

// https://docs.gradle.org/current/javadoc/org/gradle/plugin/management/PluginManagementSpec.html
// https://docs.gradle.org/current/userguide/custom_plugins.html#note_for_plugins_published_without_java_gradle_plugin
// https://docs.gradle.org/current/userguide/custom_plugins.html#sec:custom_plugins_standalone_project
// https://docs.gradle.org/current/userguide/java_gradle_plugin.html#java_gradle_plugin
// https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_markers
// https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "io.slugstack") {
                useModule("io.slugstack.oss:slugstack-publishing-plugin:${requested.version}")
            }
            // if (requested.id.namespace == "org.openrewrite") {
            //     useModule("org.openrewrite:gradle-openrewrite-project-plugin:${requested.version}")
            // }
        }
    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        mavenLocal()
    }
}
