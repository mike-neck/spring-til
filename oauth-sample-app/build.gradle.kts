import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("kotlin-spring")
    id("org.springframework.boot")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

tasks {
    "compileKotlin"(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
    }
    "compileTestKotlin"(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
    }
}

repositories {
	mavenCentral()
}

val kotlinVersion by project
val springCloudVersion by project

dependencies {
	compile("org.springframework.cloud:spring-cloud-starter-oauth2")
	compile("org.springframework.boot:spring-boot-starter-data-jpa")
	compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:${kotlinVersion}")
	compile("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
	runtime("com.h2database:h2")
	testCompile("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
	}
}
