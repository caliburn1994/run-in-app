fun properties(key: String) = project.findProperty(key).toString()


plugins {
    java
    id("org.jetbrains.intellij") version "1.17.4"
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
}

group = "github.caliburn1994"
version = "1.1.0"
val lombokVersion = "1.18.24"
val junitVersion = "5.9.0"
val assertjVersion = "3.6.1"
val intellijVersion="2024.1"

// config of gradle
// JDK version
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}



dependencies {
    // Lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")

    //
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set(intellijVersion)
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}


tasks {
    wrapper {
        jarFile = file(System.getProperty("user.dir") + "/gradle/wrapper/gradle-wrapper.jar")
        gradleVersion = "8.10.1"
        distributionType = Wrapper.DistributionType.ALL
    }
    patchPluginXml {
        sinceBuild.set(intellijVersion.replace("0",""))
        untilBuild.set("")
    }
}
