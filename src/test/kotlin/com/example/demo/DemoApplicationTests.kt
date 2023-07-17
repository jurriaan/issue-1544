package com.example.demo

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DemoApplicationTests(
) {
	@Autowired
	lateinit var personRepository: PersonRepository

	@Test
	fun contextLoads() {
		val person = Person(
			roles = listOf(Role.ADMIN)
		)

		val result = runBlocking {
			val id = personRepository.save(person).id!!
			personRepository.findById(id)!!
		}

		assert(result.roles == listOf(Role.ADMIN))
	}
}
