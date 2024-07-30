pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "lafyuu"
include(":app")
include(":core:network")
include(":core:common")
include(":core:design-system")
include(":core:datastore")
include(":core:database")
include(":feature:auth")
include(":feature:home")
include(":feature:product")
include(":feature:cart")
include(":feature:checkout")
include(":feature:orders")
