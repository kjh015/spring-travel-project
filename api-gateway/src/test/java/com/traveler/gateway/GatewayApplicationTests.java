package com.traveler.gateway;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Disabled in CI: this is the Spring Initializr smoke test and it boots the whole
// application context. It fails without environment variables and without the external
// services the context connects to. Remove @Disabled after adding a test profile.
@SpringBootTest
@Disabled("Boots the full Spring context; needs env vars and live DB/Redis/Kafka/ES. See comment above.")
class GatewayApplicationTests {

    @Test
    void contextLoads() {}
}
