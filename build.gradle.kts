plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "spotlight"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation ("mysql:mysql-connector-java:8.0.32")
    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation ("io.jsonwebtoken:jjwt:0.9.1")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("org.mapstruct:mapstruct:1.5.4.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.4.Final")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("io.github.cdimascio:java-dotenv:5.2.2")
    implementation("io.github.flashvayne:chatgpt-spring-boot-starter:1.0.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
