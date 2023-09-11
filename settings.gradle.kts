pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            // Look for POMs and artifacts, such as JARs, here
            url = uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
            // Look for artifacts here if not found at the above location
            credentials {
                // Be sure to add these non-sensitive credentials in order to retrieve dependencies from
                // the private repository.
                username = ("paypal_sgerritz")
                password =
                    ("AKCp8jQ8tAahqpT5JjZ4FRP2mW7GMoFZ674kGqHmupTesKeAY2G8NcmPKLuTxTGkKjDLRzDUQ")
            }
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            // Look for POMs and artifacts, such as JARs, here
            url = uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
            // Look for artifacts here if not found at the above location
            credentials {
                // Be sure to add these non-sensitive credentials in order to retrieve dependencies from
                // the private repository.
                username = ("paypal_sgerritz")
                password =
                    ("AKCp8jQ8tAahqpT5JjZ4FRP2mW7GMoFZ674kGqHmupTesKeAY2G8NcmPKLuTxTGkKjDLRzDUQ")
            }
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Paypal INCL"
include(":app")
 