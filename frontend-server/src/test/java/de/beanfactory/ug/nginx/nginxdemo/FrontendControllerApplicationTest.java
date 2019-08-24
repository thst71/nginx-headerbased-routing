package de.beanfactory.ug.nginx.nginxdemo;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        , classes = {ITConfiguration.class
        , FrontendControllerConfiguration.class}
)
@AutoConfigureWebTestClient
public class FrontendControllerApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CallBackendService callBackendServiceMock;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("backend.server", "http://localhost:9000");
        System.setProperty("header.name", "X-BACKEND");
        System.setProperty("header.values", "Peter");
    }

    @Test
    public void itShouldRespondToHelloEndpoint() throws Exception {
        webTestClient.get().uri("/hello")
                .exchange().expectStatus().isOk();

        Mockito.verify(callBackendServiceMock, Mockito.atLeastOnce()).call(Mockito.any());
    }

}

@TestConfiguration
class ITConfiguration {
    @Bean
    @Primary
    CallBackendService callBackendServiceMock() {
        final CallBackendService mock = Mockito.mock(CallBackendService.class);
        Mockito.when(mock.call(Mockito.any())).thenReturn(ResponseEntity.ok("Mock"));
        return mock;
    }
}