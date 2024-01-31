import io.izzel.taboolib.gradle.*

plugins {
    java
    id("io.izzel.taboolib") version "2.0.2"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

taboolib {
    env {
        install(UNIVERSAL, NMS_UTIL, BUKKIT_ALL, EXPANSION_JAVASCRIPT)
    }
    version {
        taboolib = "6.1.0"
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
//    compileOnly("ink.ptms.core:v11604:11604")
    compileOnly("com.google.guava:guava:32.1.3-jre")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}