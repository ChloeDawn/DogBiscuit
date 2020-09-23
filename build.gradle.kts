import java.time.Instant

plugins {
  id("net.minecraftforge.gradle") version "3.0.186"
  id("org.spongepowered.mixin") version "0.7-SNAPSHOT"
  id("signing")
}

group = "dev.sapphic"
version = "1.0.0"

dependencies {
  minecraft("net.minecraftforge:forge:1.16.3-34.0.13")
  implementation("org.checkerframework:checker-qual:3.6.1")
  annotationProcessor("org.spongepowered:mixin:0.8.1:processor")
}

minecraft {
  mappings("snapshot", "20200916-1.16.2")
  runs {
    with(create("client")) {
      workingDirectory = file("run").canonicalPath
      mods.create("dogbiscuit").source(sourceSets["main"])
    }
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
}

mixin {
  add(sourceSets["main"], "mixins/dogbiscuit/refmap.json")
}

signing {
  useGpgCmd()
  sign(configurations.archives.get())
}

tasks {
  named<JavaCompile>("compileJava") {
    with(options) {
      isFork = true
      isDeprecation = true
      encoding = "UTF-8"
      compilerArgs.addAll(listOf(
        "-Xlint:all", "-parameters"
      ))
    }
  }

  named<ProcessResources>("processResources") {
    from(sourceSets["main"].resources.single {
      it.name == "biscuit.png"
    }) {
      into("/")
      rename { "pack.png" }
    }
  }

  named<Jar>("jar") {
    from("/LICENSE")
    archiveClassifier.set("forge")
    manifest.attributes(mapOf(
      "Specification-Title" to project.name,
      "Specification-Vendor" to project.group,
      "Specification-Version" to 1,
      "Implementation-Title" to project.name,
      "Implementation-Version" to project.version,
      "Implementation-Vendor" to project.group,
      "Implementation-Timestamp" to "${Instant.now()}",
      "MixinConnector" to "dev.sapphic.dogbiscuit.DogBiscuitMixins"
    ))
    finalizedBy("reobfJar")
  }
}
