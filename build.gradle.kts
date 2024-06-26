import org.springframework.cloud.contract.verifier.config.TestMode

plugins {
  java
  id("org.springframework.boot") version "3.3.0"
  id("io.spring.dependency-management") version "1.1.5"
  id("org.springframework.cloud.contract") version "4.1.3"
}

group = "com.matchtrade"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

extra["springCloudVersion"] = "2023.0.2"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-testcontainers")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("io.rest-assured:spring-web-test-client")
  testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
  testImplementation("org.testcontainers:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

contracts {
  testMode = TestMode.WEBTESTCLIENT
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.contractTest {
  useJUnitPlatform()
}
