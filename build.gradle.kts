plugins {
    java
    id("org.springframework.boot") version "3.2.7"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.jblim"
version = "v1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    // For Annotation
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    // For Unit Test Code Annotation
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    // For Log
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")

    // Spring Cloud Gateway
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter:3.2.7")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.2.7")
//    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.7")
//    implementation("org.springframework.boot:spring-boot-configuration-processor:3.2.7")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    developmentOnly("org.springframework.boot:spring-boot-devtools:3.2.7")
//    implementation("org.springframework.cloud:spring-cloud-loadbalancer:4.1.0")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.1")
//    implementation("org.springframework.boot:spring-boot-starter-security:3.3.1")

    // R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:3.2.7")
    // mariadb
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    implementation("org.mariadb:r2dbc-mariadb:1.2.1")
    // flyway
    implementation("org.flywaydb:flyway-core:10.15.2")
    implementation("org.flywaydb:flyway-mysql:10.15.2")

    // Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.1.0")

//    implementation("io.projectreactor.addons:reactor-extra")

    // For Test
//    testImplementation(platform(Dependencies.JUNIT.BOM))
//    testImplementation(Dependencies.JUNIT.JUPITER)
//    testRuntimeOnly(Dependencies.JUNIT.PLATFORM_LAUNCH)
//    testRuntimeOnly(Dependencies.JUNIT.JUPITER_ENGINE)
//    testRuntimeOnly("org.junit.platform:junit-platform-reporting:1.10.1")
//    testImplementation(Dependencies.JUNIT.MOCKITO)
}

tasks.named<Test>("test") {
    useJUnitPlatform() // <5>

    testLogging {
        events("passed", "skipped", "failed") // <6>
    }
}