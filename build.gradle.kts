import io.izzel.taboolib.gradle.Bukkit
import io.izzel.taboolib.gradle.BukkitNMSUtil
import io.izzel.taboolib.gradle.CommandHelper
import io.izzel.taboolib.gradle.JavaScript

plugins {
    java
    id("io.izzel.taboolib") version "2.0.17"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

taboolib {
    env {
        install(BukkitNMSUtil, CommandHelper, JavaScript, Bukkit)
    }
    version {
        taboolib = "6.2.0-beta5"
        coroutines = null
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12101:12101:mapped")
    compileOnly("ink.ptms.core:v12101:12101:universal")
//    compileOnly("ink.ptms.core:v11604:11604")
    compileOnly("com.google.guava:guava:32.1.3-jre")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}