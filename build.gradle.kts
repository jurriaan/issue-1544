import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	// Fails with executeMany; SQL [INSERT INTO people (created_at, updated_at, roles) VALUES ($1, $2, $3)]; null value in column "roles" of relation "people" violates not-null constraint
	// org.springframework.dao.DataIntegrityViolationException: executeMany; SQL [INSERT INTO people (created_at, updated_at, roles) VALUES ($1, $2, $3)]; null value in column "roles" of relation "people" violates not-null constraint
	id("org.springframework.boot") version "3.1.1"
	// Works:
//	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.data:spring-data-r2dbc")
	implementation("org.springframework.data:spring-data-relational")
	implementation("org.postgresql:r2dbc-postgresql:1.0.2.RELEASE")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
