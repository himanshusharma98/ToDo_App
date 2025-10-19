plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.netflix.dgs.codegen") version "7.0.3"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "ToDo-Backend"

java {
    toolchain {
        // ✅ Use a stable LTS Java version (not 24)
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    // ✅ Ensure these 3 repositories are added
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

extra["springModulithVersion"] = "1.4.3"

dependencies {
    // --- Spring Core ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // --- Spring Modulith ---
    implementation("org.springframework.modulith:spring-modulith-starter-core")

    // --- Netflix DGS GraphQL ---
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter:9.1.3")
    implementation("com.graphql-java:graphql-java:21.0")
    implementation("com.graphql-java:java-dataloader:3.3.0")


    // --- Lombok ---
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // --- DevTools & DB Drivers ---
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    runtimeOnly("com.mysql:mysql-connector-j")

    // --- Testing ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
    }
}

// ✅ Correct DGS code generation task
tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    schemaPaths.add("${projectDir}/src/main/resources/schema")
    packageName = "com.example.todobackend.codegen"
    generateClient = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}
