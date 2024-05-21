plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.4")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
    implementation ("jakarta.validation:jakarta.validation-api:3.0.0")
    implementation ("org.hibernate.validator:hibernate-validator:7.0.1.Final")
    implementation ("org.glassfish:jakarta.el:3.0.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.0")
    implementation("com.rabbitmq:amqp-client")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

}
tasks.withType<Test> {
    useJUnitPlatform()
}

val integrationTestTask = tasks.register<Test>("integrationTest"){
    group = "verification"
    filter{
        includeTestsMatching("*TestIntegration")
    }
}

tasks.test{
    filter{
        includeTestsMatching("*Tests")
    }
}
