import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

var flywayCoreVersion = "9.15.0"

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("kapt") version "1.9.10"
    id("org.flywaydb.flyway") version "9.15.0"
}

group = "cz.cvut.fit.sp1"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val springBootStarterMailVersion = "3.2.5"
val jakartaValidationApiVersion = "3.0.2"
val commonsLang3Version = "3.14.0"
val vavrVersion = "0.10.4"
val springdocOpenapiVersion = "2.5.0"
val postgresqlVersion = "42.7.3"
val mapstructVersion = "1.5.5.Final"
val lombokVersion = "1.18.24"
val mapstructProcessorVersion = "1.6.0.Beta1"
val httpclientVersion = "5.2.1"
val h2Version = "2.2.222"
val jwtVersion = "0.11.5"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-mail:$springBootStarterMailVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:$jakartaValidationApiVersion")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("io.vavr:vavr:$vavrVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:$springBootStarterMailVersion")
    implementation("org.flywaydb:flyway-core:$flywayCoreVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    /*
        implementation("org.flywaydb:flyway-database-postgresql:10.11.1")
    */

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2:$h2Version")

    /*
     * Auto dto mapper
     * */
    kapt("org.mapstruct:mapstruct-processor:$mapstructProcessorVersion")

    api("org.apache.httpcomponents.client5:httpclient5:$httpclientVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
