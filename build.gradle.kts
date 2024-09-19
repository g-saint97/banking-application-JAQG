plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.google.cloud.tools.jib") version "3.3.2"
}

group = "com.jaqg"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib {
	from {
		image = "eclipse-temurin:17-jre-focal"
	}
	to {
		image = "banking:latest"
	}
	container {
		entrypoint = listOf("bash", "-c", "/entrypoint.sh")
		ports = listOf("8080")
		environment = mapOf(
			"SPRING_OUTPUT_ANSI_ENABLED" to "ALWAYS",
			"J_SLEEP" to "0"
		)
		creationTime = "USE_CURRENT_TIMESTAMP"
		user = "1000"
	}
	extraDirectories {
		setPaths("src/main/docker/jib")
		permissions = mapOf(
			"/entrypoint.sh" to "755"
		)
	}
}
