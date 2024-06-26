import org.springframework.cloud.contract.verifier.config.TestMode

plugins {
  java
  id("org.springframework.boot") version "3.3.0"
  id("io.spring.dependency-management") version "1.1.5"
  id("org.springframework.cloud.contract") version "4.1.3"
}

group = "com.matchtrade"

version = "0.0.1-SNAPSHOT"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

repositories { mavenCentral() }

extra["springCloudVersion"] = "2023.0.2"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.postgresql:postgresql")
  compileOnly("org.projectlombok:lombok:1.18.32")
  annotationProcessor("org.projectlombok:lombok:1.18.32")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
  testImplementation("io.rest-assured:spring-web-test-client")
  testImplementation("org.jeasy:easy-random-core:5.0.0")
  testImplementation("io.projectreactor:reactor-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("org.assertj:assertj-core:3.23.1")
  testImplementation("org.mock-server:mockserver-netty:5.14.0")
  testImplementation("org.mock-server:mockserver-client-java:5.14.0")
  testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
}

dependencyManagement {
  imports {
    mavenBom(
        "org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

contracts {
  testMode = TestMode.WEBTESTCLIENT
  baseClassForTests = "com.matchtrade.javaunittests.domain.ContractVerifierBase"
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.contractTest { useJUnitPlatform() }
