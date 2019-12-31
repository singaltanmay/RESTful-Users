package com.example.restfulusers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class RestfulUsersApplicationTests {

	@Test
	void contextLoads() {

		System.out.println(UUID.randomUUID().toString().length());

	}

}
