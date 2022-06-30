fun properties(key: String) = project.findProperty(key).toString()


plugins {
    java
    id("org.jetbrains.intellij") version "1.6.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
}

group = "icu.kyakya"
version = "1.0.4"




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
    version.set("2022.1.3")
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}



tasks {
    wrapper {
        jarFile = file(System.getProperty("user.dir") + "/gradle/wrapper/gradle-wrapper.jar")
        gradleVersion = "7.1"
        distributionType = Wrapper.DistributionType.ALL
    }

}
