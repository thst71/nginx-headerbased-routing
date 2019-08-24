package de.beanfactory.ug.nginx.nginxdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;
import java.util.Random;

import static java.lang.String.format;

@SpringBootApplication
public class NginxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NginxDemoApplication.class, args);
    }

}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class FrontendControllerConfiguration {
    @Value(value = "${header.name}")
    String headerName;
    @Value(value = "${backend.server}")
    String backendServer;

    @Qualifier("headervalues")
    @Bean
    String[] headerValues(@Value(value = "${header.values}") String headerValues) {
        return Optional.ofNullable(headerValues).orElse("defaultvalue").split(",");
    }

    @Qualifier("headername")
    @Bean
    String headerName() {
        return headerName;
    }

    @Qualifier("backendserver")
    @Bean
    String backendServer() {
        return backendServer;
    }
}

@RestController
class FrontendController {

    private CallBackendService callBackendService;

    @Autowired
    public FrontendController(CallBackendService callBackendService) {
        this.callBackendService = callBackendService;
    }

    @GetMapping(path = "/hello")
    public ResponseEntity<String> hello() {
        return callBackendService.call("/hello");
    }

}

@Component
class CallBackendService {
    private static Logger log = LoggerFactory.getLogger("Frontend");
    private String backendServer;
    private String headerName;
    private String[] headerValue;
    private final RestTemplate restTemplate;
    private final Random randomNumbers;

    public CallBackendService(@Qualifier("headervalues") String[] headerValue,
                              @Qualifier("headername") String headerName,
                              @Qualifier("backendserver") String backendServer,
                              RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.randomNumbers = new Random();
        this.headerValue = headerValue;
        this.headerName = headerName;
        this.backendServer = backendServer;
        log.info("FRONTEND: Routing to {} using header {}, variations: {}", backendServer, headerName, String.join(" - ", headerValue));
    }

    ResponseEntity<String> call(String path) {
        final String selectedHeader = headerValue[Math.abs(randomNumbers.nextInt()) % headerValue.length];
        final URI backendService = URI.create(format("%s%s", backendServer, path));
        log.info("FRONTEND: Routing to {} using header {} = {}", backendService, headerName, selectedHeader);
        RequestEntity entity = RequestEntity.get(backendService)
                .header(headerName, selectedHeader)
                .build();
        return restTemplate.exchange(entity, String.class);
    }

}