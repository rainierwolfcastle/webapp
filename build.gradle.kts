import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // vertx helper plugin
    id("io.vertx.vertx-plugin").version("0.5.0")

    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    id("org.jetbrains.kotlin.jvm").version("1.3.21")
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // JSON marshalling
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4.1")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.4")

    // Logging library used by db framework
    compile("org.slf4j:slf4j-jdk14:1.7.21")

    // Db framework
    compile("org.jetbrains.exposed:exposed:0.13.7")

    // web framework
    compile("io.vertx:vertx-web:3.7.0")
    compile("io.vertx:vertx-lang-kotlin:3.7.0")
    compile("io.vertx:vertx-lang-kotlin-coroutines:3.7.0")
    
    // Pg JDBC drivers
    compile("io.vertx:vertx-jdbc-client:3.7.0")
    compile("org.postgresql:postgresql:42.2.5.jre7")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")    

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")    
}

// Override vertx task
vertx {
    // Where the service lives
    mainVerticle = "webapp.Server"
    // pin a vertx version
    vertxVersion = "3.7.0"
}

// Kotlin won't if you don't target JVMs >= v1.8
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}