plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.5.2"
}

group = "com.github.avpyanov"
version = "1.0"

repositories {
    mavenCentral();
    maven("https://jitpack.io")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2021.2")
    type.set("IU") // Target IDE Platform
    plugins.set(listOf("com.intellij.java"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }
}

dependencies {
    implementation("io.github.openfeign:feign-gson:11.8")
    implementation("io.github.openfeign:feign-okhttp:11.8")
    implementation("io.github.openfeign:feign-core:11.8")
    implementation("io.github.openfeign.form:feign-form:3.8.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("com.github.avpyanov:testit-client:0.2.3")
    implementation ("org.apache.poi:poi-ooxml:5.2.2")
    implementation ("org.apache.poi:poi:5.2.2")
    implementation("org.slf4j:slf4j-api:2.0.0")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor( "org.projectlombok:lombok:1.18.22")
}