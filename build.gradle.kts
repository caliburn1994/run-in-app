fun properties(key: String) = project.findProperty(key).toString()


plugins {
    java
    id("org.jetbrains.intellij") version "1.1.4"
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
}

group = "icu.kyakya"
version = "1.0.2"


// config of gradle
// JDK version
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.2.4")
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}



tasks {
    wrapper {
        jarFile = file(System.getProperty("user.dir") + "/gradle/wrapper/gradle-wrapper.jar")
        gradleVersion = "7.1"
        distributionType = Wrapper.DistributionType.ALL
    }

}
