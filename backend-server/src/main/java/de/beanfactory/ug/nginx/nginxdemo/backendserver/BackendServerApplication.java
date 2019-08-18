package de.beanfactory.ug.nginx.nginxdemo.backendserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class BackendServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendServerApplication.class, args);
    }

}

@RestController
class BackendController  {
    @GetMapping(path = "/hello" , produces = "text/plain")
    public String hello(HttpServletRequest request) {
        List<String> output = new ArrayList<>();
        request.getHeaderNames().asIterator().forEachRemaining(headername -> output.add(String.format("%s: %s", headername, request.getHeader(headername))));
        output.sort(Comparator.naturalOrder());
        output.add(0, "[HEADER] : [VALUE]");
        output.add(String.format("This is Instance %s", System.getenv("BACKEND")));
        return String.join("\n", output);
    }
}