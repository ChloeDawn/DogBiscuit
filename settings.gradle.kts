rootProject.name = "DogBiscuit"

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://files.minecraftforge.net/maven")
    maven("https://repo.spongepowered.org/maven")
  }
  resolutionStrategy {
    eachPlugin {
      if ("net.minecraftforge.gradle" == requested.id.id) {
        useModule("net.minecraftforge.gradle:ForgeGradle:${requested.version}")
      }
      if ("org.spongepowered.mixin" == requested.id.id) {
        useModule("org.spongepowered:mixingradle:0.7-SNAPSHOT")
      }
    }
  }
}
