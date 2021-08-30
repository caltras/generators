package <%= application.package %>.controller;

import <%= application.package %>.config.AppConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ApplicationController {

    private AppConfig appConfig;

    public ApplicationController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping
    public Mono<Map<String, String>> info() {
        Map<String, String> status = new HashMap<>();
        status.put("application", this.appConfig.getApplicationName());
        status.put("buildVersion", this.appConfig.getBuildVersion());
        return Mono.just(status);
    }
}
