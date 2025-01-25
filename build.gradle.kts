plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.4.4"
}

version = "0.1"
group = "fenhtml"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("info.picocli:picocli-codegen")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("info.picocli:picocli")
    implementation("io.micronaut:micronaut-http-server")
    implementation("io.micronaut.views:micronaut-views-thymeleaf")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    runtimeOnly("ch.qos.logback:logback-classic")
}


application {
    mainClass = "fenhtml.FenhtmlCommand"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}



micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("fenhtml.*")
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}


