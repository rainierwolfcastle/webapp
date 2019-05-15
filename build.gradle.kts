import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
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
    implementation("io.vertx:vertx-rx-java2:3.7.0")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")

    implementation("io.vertx:vertx-web:3.7.0")    
    implementation("io.vertx:vertx-lang-kotlin:3.7.0")    
    // compile("io.vertx:vertx-lang-kotlin-coroutines:3.7.0")
    
    implementation("io.vertx:vertx-jdbc-client:3.7.0")
    implementation("org.postgresql:postgresql:42.2.5.jre7")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")    

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")    
}

vertx {
    mainVerticle = "webapp.Server"
    vertxVersion = "3.7.0"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}