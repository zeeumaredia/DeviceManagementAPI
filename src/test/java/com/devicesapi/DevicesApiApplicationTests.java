package com.devicesapi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Disabled("Disabled: contextLoads fails without Docker PostgreSQL running")
class DevicesApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
