package com.example.RedisProject;

import com.example.RedisProject.controller.QueueClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisProjectApplicationTests {
	@Autowired
	private QueueClass controller;
	@Test
	void contextLoads()throws Exception {
		assertThat(controller).isNotNull();
	}

}
