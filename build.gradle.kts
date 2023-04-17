import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom")
    kotlin("jvm").version(System.getProperty("kotlin_version"))
}

base { archivesName.set(extra["archives_base_name"] as String) }

val versionMinecraft = extra["minecraft_version"] as String

version = "${extra["mod_version"]}+$versionMinecraft"
group = extra["maven_group"] as String

val modId = extra["mod_id"] as String
val versionYarn = extra["yarn_build"] as String
val versionJava = extra["java_version"] as String
val versionLoader = extra["loader_version"] as String
val versionFabricApi = extra["fabric_version"] as String
val versionFabricKotlin = extra["fabric_language_kotlin_version"] as String

dependencies {
    minecraft("com.mojang", "minecraft", versionMinecraft)
    mappings("net.fabricmc", "yarn", "$versionMinecraft+build.$versionYarn", null, "v2")
    modImplementation("net.fabricmc", "fabric-loader", versionLoader)
    modImplementation("net.fabricmc.fabric-api", "fabric-api", versionFabricApi)
    include(modImplementation("net.fabricmc", "fabric-language-kotlin", versionFabricKotlin))
}

tasks {
    val javaVersion = JavaVersion.toVersion(versionJava.toInt())
    val javaVersionString = javaVersion.toString()

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersionString
        targetCompatibility = javaVersionString
        options.release.set(javaVersionString.toInt())
    }

    withType<KotlinCompile> { kotlinOptions { jvmTarget = javaVersionString } }

    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersionString)) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }

    jar { from("LICENSE") { rename { "${it}_${base.archivesName.get()}" } } }
    processResources { filesMatching("fabric.mod.json") { expand(mapOf("version" to version)) } }
}

/* Data Generation */

val generatedResourcesDir = "src/main/generated"

loom {
    accessWidenerPath.set(file("src/main/resources/$modId.accesswidener"))

    runs {
        create("Data Generation") {
            server()

            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file(generatedResourcesDir)}")
            vmArg("-Dfabric-api.datagen.modid=$modId")

            runDir("build/datagen")
        }
    }
}

sourceSets.main {
    resources {
        srcDir(generatedResourcesDir)
    }
}
