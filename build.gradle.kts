import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.1" apply false
	id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
	kotlin("jvm") version "1.6.21" apply false
	kotlin("plugin.spring") version "1.6.21" apply false
	kotlin("plugin.jpa") version "1.6.21" apply false
	kotlin("plugin.allopen") version "1.6.21" apply false
	kotlin("plugin.noarg") version "1.6.21" apply false
}
allprojects{
	group = "kr.mashup"
	version = "0.0.1-SNAPSHOT"
	tasks.withType<JavaCompile>{
		sourceCompatibility = "11"
		targetCompatibility = "11"
	}
	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}
	tasks.withType<Test> {
		useJUnitPlatform()
	}
	repositories {
		mavenCentral()
	}
}
