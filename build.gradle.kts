fun properties(key: String) = project.findProperty(key).toString()


plugins {
    java
    id("org.jetbrains.intellij") version "1.10.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.22"
}

group = "icu.kyakya"
version = "1.0.7"


// config of gradle
// JDK version
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

    //
    testImplementation("org.assertj:assertj-core:3.6.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2022.3")
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}



tasks {
    wrapper {
        jarFile = file(System.getProperty("user.dir") + "/gradle/wrapper/gradle-wrapper.jar")
        gradleVersion = "7.3"
        distributionType = Wrapper.DistributionType.ALL
    }

}
