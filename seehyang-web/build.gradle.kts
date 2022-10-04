import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

val grpcVersion = "3.19.4"
val grpcKotlinVersion = "1.2.1"
val grpcProtoVersion = "1.44.1"


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}


dependencies {
    implementation(project(":seehyang-business"))
    implementation(project(":seehyang-core"))
    implementation(project(":seehyang-redis"))
    implementation("com.auth0:java-jwt:3.18.3")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    //implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.data:spring-data-commons")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

}
