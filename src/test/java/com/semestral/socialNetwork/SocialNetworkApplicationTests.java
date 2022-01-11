package com.semestral.socialNetwork;

import com.semestral.socialNetwork.controller.UserController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
class SocialNetworkApplicationTests {

	@Autowired
	UserController userController;

	@Test
	void contextLoads() {
		Assertions.assertThat(userController).isNotNull();
	}

}
