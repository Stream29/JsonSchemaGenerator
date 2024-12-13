package buildsrc.convention

import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.kotlin.dsl.`maven-publish`
import java.net.URI

plugins {
    `maven-publish`
    signing
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates((group as String), name, version.toString())
    pom {
        name.set("JsonSchemaGenerator")
        description.set("KMP library for generate JSON schema from @Serializable classes")
        url.set("https://github.com/Stream29/JsonSchemaGenerator")

        licenses {
            license {
                name = "The Apache Software License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                name.set("Stream")
            }
        }
        scm {
            url.set("https://github.com/Stream29/JsonSchemaGenerator")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/Stream29/JsonSchemaGenerator")
            credentials {
                username = System.getenv("GITHUB_ACTOR")!!
                password = System.getenv("GITHUB_TOKEN")!!
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
