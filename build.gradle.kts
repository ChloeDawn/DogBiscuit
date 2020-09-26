import java.time.Instant

plugins {
  id("fabric-loom") version "0.5.28"
  id("signing")
}

group = "dev.sapphic"
version = "1.0.0"

dependencies {
  fun embeddedApiModule(module: String): Dependency? {
    val notation = fabricApi.module(module, "0.21.0+build.407-1.16")
    return modImplementation(include(notation)!!)
  }

  minecraft("com.mojang:minecraft:1.16.3")
  mappings("net.fabricmc:yarn:1.16.3+build.17:v2")
  modImplementation("net.fabricmc:fabric-loader:0.9.3+build.207")
  embeddedApiModule("fabric-api-base")
  embeddedApiModule("fabric-networking-v0")
  embeddedApiModule("fabric-registry-sync-v0")
  embeddedApiModule("fabric-resource-loader-v0")
  implementation("org.jetbrains:annotations:20.1.0")
  implementation("org.checkerframework:checker-qual:3.6.1")
  modRuntime("io.github.prospector:modmenu:1.14.6+build.31") {
    isTransitive = false
  }
}

minecraft {
  refmapName = "mixins/dogbiscuit/refmap.json"
  runDir = "run"
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
}

signing {
  useGpgCmd()
  sign(configurations.archives.get())
}

tasks {
  compileJava {
    with(options) {
      isFork = true
      isDeprecation = true
      encoding = "UTF-8"
      compilerArgs.addAll(listOf(
        "-Xlint:all", "-parameters"
      ))
    }
  }

  processResources {
    filesMatching("/fabric.mod.json") {
      expand("version" to project.version)
    }
    from(sourceSets["main"].resources.single {
      it.name == "biscuit.png"
    }) {
      into("/")
      rename { "pack.png" }
    }
  }

  jar {
    from("/LICENSE")
    archiveClassifier.set("fabric") // FIXME
    manifest.attributes(mapOf(
      "Specification-Title" to project.name,
      "Specification-Vendor" to project.group,
      "Specification-Version" to 1,
      "Implementation-Title" to project.name,
      "Implementation-Version" to project.version,
      "Implementation-Vendor" to project.group,
      "Implementation-Timestamp" to "${Instant.now()}"
    ))
  }
}
