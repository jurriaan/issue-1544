package com.example.demo

import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.Option
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Lazy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.EnumWriteSupport
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.scheduling.annotation.EnableAsync
import java.time.Instant

@SpringBootApplication
@EnableR2dbcRepositories(
	includeFilters = [
		ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = [CoroutineCrudRepository::class, ReactiveCrudRepository::class, R2dbcRepository::class]
		)
	],
	basePackages = ["com.example.demo"],
)
@EntityScan("com.example.demo")
@EnableAsync
@EnableR2dbcAuditing()
class DemoApplication {
	@Bean
	fun r2dbcCustomConversions(
		@Lazy mappingR2dbcConverter: MappingR2dbcConverter,
	): R2dbcCustomConversions =
		R2dbcCustomConversions.of(
			PostgresDialect.INSTANCE,
			listOf(
				Role.EnumConverter
			)
		)

	@Bean
	fun r2dbcCustomizeBuilder(): ConnectionFactoryOptionsBuilderCustomizer = ConnectionFactoryOptionsBuilderCustomizer {
		it.option(
			Option.valueOf("extensions"),
			listOf(
				EnumCodec.builder().withEnum("role_type", Role::class.java).build(),
			)
		)
	}
}

enum class Role {
	ADMIN,
	USER,
	;

	object EnumConverter : EnumWriteSupport<Role>()
}

@Table(name = "people")
class Person(
	@Id
	var id: Long? = null,

	@CreatedDate
	var createdAt: Instant? = null,

	@LastModifiedDate
	var updatedAt: Instant? = null,

	var roles: List<Role>,
)

interface PersonRepository : CoroutineCrudRepository<Person, Long>

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

